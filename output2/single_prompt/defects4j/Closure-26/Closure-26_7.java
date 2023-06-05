## Fixed Function 1
private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
    Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
    Node ifNode = IR.ifNode(moduleExportsProp!=null ? IR.TRUE : IR.FALSE,
                            IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp.cloneTree()))));
    ifNode.setLineno(script.getLineno());
    ifNode.setCharno(script.getCharno());
    script.addChildToBack(ifNode);
}

## Fixed Function 2
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = prop.getFirstChild();
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    module.setString(moduleName);
    Node exports = module.getNext();
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    exports.setString("module$exports");
}