## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    new NodeTraversal(compiler, new NormalizeStatements(compiler, assertOnChange)).traverse(root);
    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(true);
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseRoots(externs, root);
    }
    removeDuplicateDeclarations(externs, root);
    new PropagateConstantAnnotationsOverVars(compiler, assertOnChange).process(externs, root);
    if (!compiler.getLifeCycleStage().isNormalized()) {
        compiler.setLifeCycleStage(LifeCycleStage.NORMALIZED);
    }
}

## Fixed Function 2
private void createSynthesizedExternVar(String varName) {
    Node nameNode = IR.name(varName);
    // Mark the variable as constant if it matches the coding convention
    // for constant vars.
    if (compiler.getCodingConvention().isConstant(varName)) {
        nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    }
    Node varNode = IR.var(nameNode);
    getSynthesizedExternsRoot().addChildToBack(varNode);
    varsToDeclareInExterns.remove(varName);
}