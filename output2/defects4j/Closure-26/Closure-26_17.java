## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifNode = new Node(Token.IF);
    Node condition = IR.not(IR.isNull(moduleExportsProp));
    Node block = IR.block(
        IR.exprResult(
            IR.assign(
                IR.name(moduleName),
                moduleExportsProp.cloneTree()
            )
        )
    );
    ifNode.addChildToBack(condition);
    ifNode.addChildToBack(block);
    script.addChildToBack(ifNode.copyInformationFromForTree(script));
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = Node.newString(Token.NAME, "module");
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    module.setString(moduleName);
    Node exports = Node.newString(Token.NAME, "exports");
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    exports.setString("module$exports");
    prop.replaceChild(prop.getChildAtIndex(0), module);
    prop.replaceChild(prop.getChildAtIndex(1), exports);
}