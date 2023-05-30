void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    Preconditions.checkArgument(inferred || type != null);
    // Only allow declarations of NAMEs and qualfied names.
    boolean shouldDeclareOnGlobalThis = false;
    if (n.getType() == Token.NAME) {
        Preconditions.checkArgument(parent.getType() == Token.FUNCTION || parent.getType() == Token.VAR || parent.getType() == Token.LP || parent.getType() == Token.CATCH);
        shouldDeclareOnGlobalThis = scope.isGlobal() && (parent.getType() == Token.VAR || parent.getType() == Token.FUNCTION);
    } else {
        Preconditions.checkArgument(n.getType() == Token.GETPROP && (parent.getType() == Token.ASSIGN || parent.getType() == Token.EXPR_RESULT));
    }
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(variableName != null && !variableName.isEmpty());
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
        scopeToDeclareIn.declare(variableName, n, type, input, inferred);
        if (shouldDeclareOnGlobalThis) {
            ObjectType globalThis = typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
            boolean isExtern = input.isExtern();
            JSType declaredType = (type == null) ? getNativeType(JSTypeNative.NO_TYPE) : type;
            if (inferred) {
                globalThis.defineInferredProperty(variableName, declaredType, isExtern);
            } else {
                globalThis.defineDeclaredProperty(variableName, declaredType, isExtern);
            }
        }
        // If we're in the global scope, also declare var.prototype
        // in the scope chain.
        if (scopeToDeclareIn.isGlobal() && type instanceof FunctionType) {
            FunctionType fnType = (FunctionType) type;
            if (fnType.isConstructor() || fnType.isInterface()) {
                FunctionType superClassCtor = fnType.getSuperClassConstructor();
                boolean declared = superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE));
                scopeToDeclareIn.declare(variableName + ".prototype", n, fnType.getPrototype(), input, declared);
            }
        }
    }
}