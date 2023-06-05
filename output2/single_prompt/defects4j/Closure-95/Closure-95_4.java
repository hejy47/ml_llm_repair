void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    Preconditions.checkArgument(inferred || type != null);
    // Only allow declarations of NAMEs and qualified names.
    boolean shouldDeclareOnGlobalThis = false;
    if (n.getType() == Token.NAME) {
        Preconditions.checkArgument(
                parent.getType() == Token.FUNCTION ||
                        parent.getType() == Token.VAR ||
                        parent.getType() == Token.LET ||
                        parent.getType() == Token.CONST ||
                        parent.getParent().getType() == Token.FOR ||
                        parent.getParent().getType() == Token.FOR_OF ||
                        parent.getParent().getType() == Token.FOR_AWAIT_OF ||
                        parent.getParent().getType() == Token.CATCH);
        shouldDeclareOnGlobalThis =
                scope.isGlobal() && (parent.getType() == Token.VAR || parent.getType() == Token.FUNCTION);
    } else {
        // Allow access to properties on functions for creating a higher-order function.
        Node root = n.getFirstChild();
        if (root != null && root.getType() == Token.NAME && scope.isDeclared(root.getString(), true)) {
            Node rootParent = root.getParent();
            shouldDeclareOnGlobalThis = rootParent.getType() == Token.FUNCTION && n.getType() == Token.GETPROP;
        } else {
            Preconditions.checkArgument(n.getType() == Token.GETPROP && (parent.getType() == Token.ASSIGN || parent.getType() == Token.EXPR_RESULT));
        }
    }
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(!variableName.isEmpty());
    // If n is a property, then we should really declare it in the scope where the root object appears. This helps out people who declare "global" names in an anonymous namespace.
    Scope scopeToDeclareIn = scope.getScope(variableName);
    if (scopeToDeclareIn.isDeclared(variableName, false)) {
        Var oldVar = scopeToDeclareIn.getVar(variableName);
        validator.expectUndeclaredVariable(sourceName, n, parent, oldVar, variableName, type);
    } else {
        if (!inferred) {
            setDeferredType(n, type);
        }
        CompilerInput input = compiler.getInput(sourceName);
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
        // If we're in the global scope, also declare var.prototype in the scope chain.
        if (scopeToDeclareIn.isGlobal() && type instanceof FunctionType) {
            FunctionType fnType = (FunctionType) type;
            if (fnType.isConstructor() || fnType.isInterface()) {
                FunctionType superClassCtor = fnType.getSuperClassConstructor();
                scopeToDeclareIn.declare(
                        variableName + ".prototype",
                        n,
                        fnType.getPrototype(),
                        compiler.getInput(sourceName),
                        /* declared iff there's an explicit supertype */
                        superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE)));
            }
        }
    }
}