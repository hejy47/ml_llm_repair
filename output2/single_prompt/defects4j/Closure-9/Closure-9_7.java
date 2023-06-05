## Fixed Function 1
private String normalizeSourceName(String filename) {
    // The DOS command shell will normalize "/" to "\", so we have to
    // wrestle it back.
    if (filename.startsWith(filenamePrefix)) { // use startswith instead of indexOf to ensure exact match
        filename = filename.substring(filenamePrefix.length());
    }
    return filename;
}

## Fixed Function 2
private void visitScript(NodeTraversal t, Node script) {
    Preconditions.checkArgument(scriptNodeCount == 1, "ProcessCommonJSModules supports only one invocation per " + "CompilerInput / script node");
    String moduleName = guessCJSModuleName(normalizeSourceName(script.getSourceFileName()));
    script.addChildToFront(IR.var(IR.name(moduleName), IR.objectlit()).copyInformationFromForTree(script));
    if (reportDependencies) {
        CompilerInput ci = t.getInput();
        ci.addProvide(moduleName);
        JSModule m = new JSModule(moduleName);
        m.addAndOverrideModule(ci);
        module = m;
    }
    script.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")), IR.string(moduleName))).copyInformationFromForTree(script));
    emitOptionalModuleExportsOverride(script, moduleName);
    // Rename vars to not conflict in global scope.
    NodeTraversal.traverse(compiler, script, new SuffixVarsCallback(moduleName));
    t.reportCodeChange(); // use the provided NodeTraversal instead of the compiler instance
}