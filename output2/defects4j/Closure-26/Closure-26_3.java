## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifNode = IR.ifNode(moduleExportsProp, IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp.cloneTree()))));
    ifNode.useSourceInfoFrom(script);
    script.addChildToBack(ifNode);
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = IR.name("module");
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    module.setString(moduleName);
    prop.replaceChild(prop.getFirstChild(), module);
    prop.putBooleanProp(Node.EXPORT_DEFAULT, true);
    Node exports = prop.getSecondChild();
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    exports.setString("module$exports");
}