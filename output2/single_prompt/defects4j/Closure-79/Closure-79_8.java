## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    new NodeTraversal(compiler, new NormalizeStatements(compiler, assertOnChange)).traverse(root);
    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseRoots(externs, root);
        removeDuplicateDeclarations(externs, root); // Fixed: Remove duplicate declarations after renaming
    } else {
        removeDuplicateDeclarations(externs, root); // Fixed: Remove duplicate declarations before propagate constant annotations
    }
    new PropagateConstantAnnotationsOverVars(compiler, assertOnChange).process(externs, root);
    if (!compiler.getLifeCycleStage().isNormalized()) {
        compiler.setLifeCycleStage(LifeCycleStage.NORMALIZED);
    }
}

## Fixed Function 2
private void createSynthesizedExternVar(String varName) {
    Node nameNode = Node.newString(Token.NAME, varName);
    // Mark the variable as constant if it matches the coding convention
    // for constant vars.
    // NOTE(nicksantos): honestly, i'm not sure how much this matters.
    // AFAIK, all people who use the CONST coding convention also
    // compile with undeclaredVars as errors. We have some test
    // cases for this configuration though, and it makes them happier.
    if (compiler.getCodingConvention().isConstant(varName)) {
        nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    }
    Node varNode = new Node(Token.VAR, nameNode);
    varNode.putBooleanProp(Node.IS_SYNTHETIC_VAR, true); // Fixed: Add IS_SYNTHETIC_VAR property
    getSynthesizedExternsRoot().addChildToBack(varNode);
    varsToDeclareInExterns.remove(varName);
}