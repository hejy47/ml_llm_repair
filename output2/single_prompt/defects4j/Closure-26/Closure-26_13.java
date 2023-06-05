## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifNode = IR.ifNode(moduleExportsProp, IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp.cloneTree()))));
    ifNode.useSourceInfoIfMissingFromForTree(script);
    script.addChildToBack(ifNode);
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = NodeUtil.newQNameNode(CompilerConstants.MODULE, moduleName);
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    prop.replaceChild(prop.getChildAtIndex(0), module);
    Node exports = NodeUtil.newQNameNode(CompilerConstants.EXPORTS, "module$exports");
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    prop.replaceChild(prop.getChildAtIndex(1), exports);
}