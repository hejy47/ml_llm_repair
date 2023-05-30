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
    n.setJSType(replacementFnType != null ? replacementFnType.getReturnType() : unknownType);
    if (replacementFnType != null) {
        callTarget.setJSType(replacementFnType);
        return replacer.madeChanges;
    } else {
        return false;
    }
}