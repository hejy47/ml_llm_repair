## Fixed Function 1
/**
 * Emits an optional module.exports override for CommonJS modules.
 * @param script The script node containing the module.
 * @param moduleName The name of the CommonJS module.
 */
 private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
     Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
     Node ifNode = IR.ifNode(moduleExportsProp.cloneTree(), IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsProp))));
     ifNode.copyInformationFrom(script);
     script.addChildToBack(ifNode);
 }

## Fixed Function 2
/**
 * Visits the module.exports statement and sets the node's original name and string value.
 * @param prop The node representing the module.exports statement.
 */
private void visitModuleExports(Node prop) {
    String moduleName = guessCJSModuleName(prop.getSourceFileName());
    Node module = prop.getFirstChild();
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    module.setString(moduleName);
    Node exports = module.getNext();
    exports.putProp(Node.ORIGINALNAME_PROP, "exports");
    exports.setString("module$exports");
}