## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifBlock = IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp.cloneTree())));
    Node ifNode = IR.ifNode(moduleExportsProp, ifBlock);
    // Set source information for the new nodes
    ifNode.copyInformationFrom(script);
    ifBlock.copyInformationFrom(script);
    script.addChildToBack(ifNode);
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = prop.getFirstChild();
    module.putProp(Node.ORIGINALNAME_PROP, null); // Clear original name property
    module.setString(moduleName);
    Node exports = prop.getLastChild();
    exports.putProp(Node.ORIGINALNAME_PROP, null); // Clear original name property
    exports.setString("module$exports");
}