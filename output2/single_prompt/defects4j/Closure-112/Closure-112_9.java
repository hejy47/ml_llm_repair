private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
    final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
    if (keys.isEmpty()) {
        return false;
    }
    // Try to infer the template types
    Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);
    // Replace all template types. If we couldn't find a replacement, we
    // replace it with UNKNOWN.
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
    Node callTarget = n.getFirstChild();
    FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();
    Preconditions.checkNotNull(replacementFnType);
    callTarget.setJSType(replacementFnType);
    if (n.hasChildren()) {
        Node paramList = callTarget.getNext();
        for (int i = 0; i < replacementFnType.getMaxArity(); i++) {
            // Set the type of the arguments to the inferred value if it exists,
            // otherwise set it to UNKNOWN.
            if (i < paramList.getChildCount()) {
                Node argNode = paramList.getChildAt(i);
                JSType argType = replacementFnType.getParameterType(i);
                if (argType != null) {
                    argNode.setJSType(argType);
                } else {
                    argNode.setJSType(nulType);
                }
            }
        }
    }
    n.setJSType(replacementFnType.getReturnType());
    return replacer.madeChanges;
}