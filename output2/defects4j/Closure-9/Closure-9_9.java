## Fixed Function 1
private String normalizeSourceName(String filename) {
    filename = filename.replace('/', '\\'); // Normalize "/" to "\"
    if (filename.startsWith(filenamePrefix)) {
        filename = filename.substring(filenamePrefix.length());
    }
    return filename;
}

## Fixed Function 2
private void visitScript(NodeTraversal t, Node script) {
    Preconditions.checkArgument(scriptNodeCount == 1, "ProcessCommonJSModules supports only one invocation per " + "CompilerInput / script node");
    String moduleName = guessCJSModuleName(normalizeSourceName(script.getSourceFileName()));
    Node varNode = IR.var(IR.name(moduleName), IR.objectlit()).useSourceInfoIfMissingFromForTree(script);
    script.addChildToFront(varNode);

    if (reportDependencies) {
        CompilerInput ci = t.getInput();
        ci.addProvide(moduleName);
        JSModule m = new JSModule(moduleName);
        m.addAndOverrideModule(ci);
        module = m;
    }

    Node googProvideCall = IR.call(IR.getprop(IR.name("goog"), IR.string("provide")), IR.string(moduleName)).useSourceInfoIfMissingFromForTree(script);
    Node exprResultNode = IR.exprResult(googProvideCall).useSourceInfoIfMissingFromForTree(script);
    script.addChildToFront(exprResultNode);

    emitOptionalModuleExportsOverride(script, moduleName);

    // Rename vars to not conflict in global scope.
    NodeTraversal.traverse(compiler, script, new SuffixVarsCallback(moduleName));
    compiler.reportChangeToEnclosingScope(varNode);
}