void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(variableName != null && !variableName.isEmpty(), "Empty variable name");

    if (n.getType() == Token.NAME) {
        // Only allow declarations of NAMEs and qualified names.
        Preconditions.checkArgument(parent.getType() == Token.FUNCTION || parent.getType() == Token.VAR || parent.getType() == Token.LET || parent.getType() == Token.CONST || parent.getType() == Token.CATCH || parent.getType() == Token.PARAM_LIST,
                                    "Invalid parent node type for NAME %s", variableName);

        // Only allow "var" declarations in the global scope
        boolean shouldDeclareOnGlobalThis = false;
        if (scope.isGlobal() && (parent.getType() == Token.VAR)) {
            shouldDeclareOnGlobalThis = true;
        }
        
        ObjectType globalThis = null;
        if (shouldDeclareOnGlobalThis) {
            globalThis = typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
        }

        // Check if the variable is already declared in the current scope
        if (scope.isDeclared(variableName, false)) {
            Var oldVar = scope.getVar(variableName);
            validator.expectUndeclaredVariable(sourceName, n, parent, oldVar, variableName, type);
        } else {
            // Declare the variable in the current scope
            scope.declare(variableName, n, type, compiler.getInput(sourceName), inferred);

            if (shouldDeclareOnGlobalThis) {
                // Declare the variable also on the global "this" object
                boolean isExtern = compiler.getInput(sourceName).isExtern();
                if (inferred) {
                    globalThis.defineInferredProperty(variableName, type == null ? getNativeType(JSTypeNative.NO_TYPE) : type, isExtern);
                } else {
                    globalThis.defineDeclaredProperty(variableName, type, isExtern);
                }
            }

            // If we're in the global scope, also declare var.prototype in the scope chain for constructor functions
            if (scope.isGlobal() && type instanceof FunctionType) {
                FunctionType fnType = (FunctionType) type;
                if (fnType.isConstructor() || fnType.isInterface()) {
                    FunctionType superClassCtor = fnType.getSuperClassConstructor();
                    scope.declare(variableName + ".prototype", n, fnType.getPrototype(), compiler.getInput(sourceName), superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE)));
                }
            }
        }

        if (!inferred) {
            setDeferredType(n, type);
        }
    } else if (n.getType() == Token.GETPROP) {
        Preconditions.checkArgument(parent.getType() == Token.ASSIGN || parent.getType() == Token.EXPR_RESULT,
                                    "Invalid parent node type for GETPROP %s", variableName);
        // Validate properties access
        Preconditions.checkState(
            !scope.isDeclared(variableName, true),
            "Getprop access to undeclared variable %s (inferred=%s)", variableName, String.valueOf(inferred)
        );
    } else {
        throw new IllegalStateException("Unexpected variable type: " + Token.name(n.getType()));
    }
}