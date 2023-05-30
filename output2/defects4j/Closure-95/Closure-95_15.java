void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    Preconditions.checkArgument(inferred || type != null);
    // Only allow declarations of NAMEs and qualfied names.
    boolean shouldDeclareOnGlobalThis = false;
    boolean isName = false;
    boolean isGetProp = false;
    if (n.getType() == Token.NAME) {
        Preconditions.checkArgument(parent.getType() == Token.FUNCTION || parent.getType() == Token.VAR || parent.getType() == Token.LP || parent.getType() == Token.CATCH);
        isName = true;
        shouldDeclareOnGlobalThis = scope.isGlobal() && (parent.getType() == Token.VAR || parent.getType() == Token.FUNCTION);
    } else if (n.getType() == Token.GETPROP) {
        Preconditions.checkArgument(parent.getType() == Token.ASSIGN || parent.getType() == Token.EXPR_RESULT);
        isGetProp = true;
    } else {
        Preconditions.checkArgument(false, "illegal node type for LHS of assignment: %s", n.getType());
    }
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(!variableName.isEmpty());
    // If n is a property, then we should really declare it in the
    // scope where the root object appears. This helps out people
    // who declare "global" names in an anonymous namespace.
    Scope scopeToDeclareIn = scope;
    // don't try to declare in the global scope if there's
    // already a symbol there with this name.
    // declared in closest scope?
    if (scopeToDeclareIn.isDeclared(variableName, false)) {
        Var oldVar = scopeToDeclareIn.getVar(variableName);
        validator.expectUndeclaredVariable(sourceName, n, parent, oldVar, variableName, type);
    } else {
        if (!inferred) {
            setDeferredType(n, type);
        }
        CompilerInput input = compiler.getInput(sourceName);
        if (isName) {
            scopeToDeclareIn.declare(variableName, n, type, input, inferred);
            if (shouldDeclareOnGlobalThis) {
                ObjectType globalThis = typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
                boolean isExtern = input.isExtern();
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
                    scopeToDeclareIn.declare(variableName + ".prototype", n, fnType.getPrototype(), compiler.getInput(sourceName), /* declared iff there's an explicit supertype */
                    superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE)));
                }
            }
        } else if (isGetProp) {
            Node obj = n.getFirstChild();
            String propName = obj.getNext().getString();
            TypeI objectType = determineType(obj);
            ObjectType objType;
            if (objectType != null) {
                objectType = objectType.restrictByNotNullOrUndefined();
            objType = objectType.autoboxElementTypeToNullableTypeIfSubclassOf(JSTypeNative.OBJECT_TYPE);
            } else {
                objType = typeRegistry.createAnonymousObjectType();
            }
            boolean isTemplateType = objType.hasReferenceToGenericType(propName);
            if (isTemplateType) {
                objType = objType.instantiateGenericsFromArgumentTypes(determineTypeStrict(parent).getTemplateTypes());
            }
            if (inferred) {
                objType.defineInferredProperty(propName, type == null ? getNativeType(JSTypeNative.NO_TYPE) : type, false);
            } else {
                objType.defineDeclaredProperty(propName, type, false);
            }
            ObjectType ownerType = null;
            if (NodeUtil.mayHaveSideEffects(obj)) {
                String tempVarName = scope.createTempName();
                Node tempVar = IR.var(IR.name(tempVarName).setJSType(objType.toJSType()), obj.detach());
                scopeToDeclareIn.declare(tempVarName, tempVar.getFirstChild(), objType.toJSType(), input, true);
                obj = tempVar.getFirstChild();
                ownerType = objectType == null ? objType : null;
            }
            Node newGetProp = IR.getprop(obj, propName);
            newGetProp.setJSType(objType.getPropertyType(propName));
            parent.replaceChild(n, newGetProp);
            n = newGetProp;
            if (ownerType != null) {
                ownerType.defineDeclaredProperty(propName, n.getJSType(), false);
            }
        }
    }
}