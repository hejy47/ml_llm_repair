void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
    Preconditions.checkArgument(inferred || type != null);
    // Only allow declarations of NAMEs and qualified names.
    boolean shouldDeclareOnGlobalThis = false;
    if (n.isName()) {
        Preconditions.checkArgument(parent.isFunction() || parent.isVar() || parent.isParamList() || parent.isCatch());
        shouldDeclareOnGlobalThis = scope.isGlobal() && (parent.isVar() || parent.isFunction());
    } else {
        Preconditions.checkArgument(n.isGetProp() && (parent.isAssign() || parent.isExprResult()));
    }
    String variableName = n.getQualifiedName();
    Preconditions.checkArgument(!variableName.isEmpty());
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
                scopeToDeclareIn.declare(variableName + ".prototype", n, fnType.getPrototype(), compiler.getInput(sourceName), /* declared iff there's an explicit supertype */
                superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(JSTypeNative.OBJECT_TYPE)));
            }
        }
    }
}