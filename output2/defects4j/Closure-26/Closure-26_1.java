## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifNode = IR.ifNode(moduleExportsProp, IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp.cloneTree()))));
    ifNode.copyInformationFrom(script);
    script.addChildToBack(ifNode);
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = prop.getFirstChild();
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    module.setString(moduleName);
    Node exports = prop.getLastChild();
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    exports.setString("module$exports");
}