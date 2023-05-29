## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifNode = IR.ifNode(moduleExportsProp, IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp.cloneTree()))));
    ifNode.setJSDocInfo(
        (new JSDocInfoBuilder(true))
            .addSuppressions("uselessCode", "12345")
            .build());
    script.addChildToBack(ifNode.copyInformationFromForTree(script));
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = prop.getFirstChild().getString();
    Node module = prop.getFirstChild();
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    Node exports = prop.getLastChild();
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    exports.setString("module$exports");
    module.setString(moduleName);
}