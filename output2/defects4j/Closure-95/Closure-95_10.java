void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    Preconditions.checkArgument(inferred || type != null);
    // Only allow declarations of NAMEs and qualfied names.
    boolean shouldDeclareOnGlobalThis = false;
    int parentType = parent.getType();
    if (n.getType() == Token.NAME) {
        Preconditions.checkArgument(parentType == Token.FUNCTION || parentType == Token.VAR || parentType == Token.LET || parentType == Token.CONST || parentType == Token.CATCH);
        shouldDeclareOnGlobalThis = scope.isGlobal() && (parentType == Token.VAR || parentType == Token.FUNCTION);
    } else {
        Preconditions.checkArgument(n.getType() == Token.GETPROP && (parentType == Token.ASSIGN || parentType == Token.EXPR_RESULT));
    }
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(variableName != null && !variableName.isEmpty());
    // If n is a property, then we should really declare it in the
    // scope where the root object appears. This helps out people
    // who declare "global" names in an anonymous namespace.
    Scope scopeToDeclareIn = scope.getClosestHoistScope();
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
        if (scope.isGlobal() && type instanceof FunctionType) {
            FunctionType fnType = (FunctionType) type;
            if (fnType.isConstructor() || fnType.isInterface()) {
                FunctionType superClassCtor = fnType.getSuperClassConstructor();
                ObjectType instanceType = fnType.getInstanceType();
                boolean isImplicitProtoAlias = n.matchesQualifiedName("prototype") && !inferred;
                scopeToDeclareIn.declare(variableName + ".prototype", n, instanceType.getPrototype(), compiler.getInput(sourceName), /* declared iff there's an explicit supertype */
                superClassCtor == null || instanceType.equals(getNativeType(OBJECT_TYPE)) || isImplicitProtoAlias);
            }
        }
    }
}