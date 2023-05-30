## Fixed Function 1
void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    Preconditions.checkArgument(inferred || type != null);
    // Only allow declarations of NAMEs and qualfied names.
    boolean shouldDeclareOnGlobalThis = false;
    if (n.getType() == Token.NAME) {
        Preconditions.checkArgument(parent.getType() == Token.FUNCTION || parent.getType() == Token.VAR || parent.getType() == Token.LET || parent.getType() == Token.CONST || parent.getType() == Token.CATCH);
        shouldDeclareOnGlobalThis = scope.isGlobal() && parent.getType() == Token.VAR;
    } else {
        Preconditions.checkArgument(n.getType() == Token.GETPROP && (parent.getType() == Token.ASSIGN || parent.getType() == Token.EXPR_RESULT));
    }
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(variableName != null && !variableName.isEmpty());
    // If n is a property, then we should really declare it in the
    // scope where the root object appears. This helps out people
    // who declare "global" names in an anonymous namespace.
    Scope scopeToDeclareIn = scope.getParent();
    // don't try to declare in the global scope if there's
    // already a symbol there with this name.
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    if (oldVar != null && !oldVar.isBleedingFunction()) {
        validator.expectUndeclaredVariable(sourceName, n, parent, oldVar, variableName, type, inferred);
    } else {
        Node enclosingBlock = NodeUtil.getEnclosingNode(n, new NodeUtil.MatchPredicate() {
            @Override
            public boolean evaluate(Node n) {
                return n.isBlock() || n.isScript();
            }
        });
        // ES6 14.3.2 - Creating Identifiers as Binding Names at Function Declaration Instantiation Time
        // Handle cases where a parent block (let/const/function) needs to be created
        if (enclosingBlock.isScript()) {
            // If we're in the global scope, only declare a global let/const if it has not
            // already been declared and its types are not inferred.
            if (!scopeToDeclareIn.isDeclared(variableName, false) && (parent.getType() == Token.LET || parent.getType() == Token.CONST) && !inferred) {
                parent.removeChildren();
                parent.addChildToFront(IR.name(variableName).useSourceInfoFrom(n));
                parent.addChildToBack(n);
                Node exprResult = parent.getParent();
                Node block = exprResult.getParent();
                Node let = NodeUtil.newVarNode(parent.detachChildren());
                let.useSourceInfoFrom(parent);
                block.addChildToFront(let);
                scopeToDeclareIn.declare(variableName, let.getFirstChild(), type, compiler.getInput(sourceName), false, inferred);
                return;
            }
        } else if (!scopeToDeclareIn.isDeclared(variableName, true)) {
            if ((parent.getType() == Token.LET || parent.getType() == Token.CONST) && !inferred) {
                if (scope.getVar(variableName) != null) {
                    validator.error("Identifier " + variableName + " has already been declared");
                    return;
                }
                // Add a new block to the parent
                Node newBlock = IR.block();
                newBlock.useSourceInfoFrom(n);
                Node blockNode = newBlock;
                for (int i = 0; i < 2; i++) {
                    Node newBlockParent = NodeUtil.getEnclosingNode(blockNode, new NodeUtil.MatchPredicate() {
                        @Override
                        public boolean evaluate(Node n) {
                            return n.isFunction() || n.isBlock() || n.isScript();
                        }
                    }).getParent();
                    Node innerBlock = IR.block(newBlock);
                    innerBlock.useSourceInfoFrom(newBlock);
                    Node stmtNode;
                    if (i == 0) {
                        stmtNode = NodeUtil.newVarNode(parent.detachChildren());
                    } else {
                        stmtNode = NodeUtil.newExpr(parent.detachChildren());
                    }
                    stmtNode.useSourceInfoFrom(n);
                    innerBlock.addChildToBack(stmtNode);
                    newBlockParent.addChildBefore(innerBlock, blockNode);
                    blockNode = innerBlock;
                }
                scopeToDeclareIn = scopeToDeclareIn.getClosestHoistScope();
                scopeToDeclareIn.declare(variableName, parent, type, compiler.getInput(sourceName), true, inferred);
                return;
            } else if (!inferred) {
                // If variable names are inferred, they cannot be added to the closest HoistScope.
                scopeToDeclareIn.declare(variableName, n, type, compiler.getInput(sourceName), false, inferred);
            } else {
                // Inferred types are always declared
                setDeferredType(n, type);
                scopeToDeclareIn.declare(variableName, n, type, compiler.getInput(sourceName), false, inferred);
            }
        }
        if (shouldDeclareOnGlobalThis) {
            ObjectType globalThis = typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
            boolean isExtern = compiler.getInput(sourceName).isExtern();
            if (inferred) {
                globalThis.defineInferredProperty(variableName, type == null ? getNativeType(JSTypeNative.NO_TYPE) : type, isExtern);
            } else {
                globalThis.defineDeclaredProperty(variableName, type, isExtern);
            }
        }
        // If we're in the global scope, also declare var.prototype
        // in the scope chain.
        if (scopeToDeclareIn.isGlobal() && type instanceof FunctionType) {
            FunctionType fnType = (FunctionType) type;
            if (fnType.isConstructor() || fnType.isInterface()) {
                FunctionType superClassCtor = fnType.getSuperClassConstructor();
                scopeToDeclareIn.declare(variableName + ".prototype", n, fnType.getPrototype(), compiler.getInput(sourceName), superClassCtor == null
                        || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE)));
            }
        }
    }
}