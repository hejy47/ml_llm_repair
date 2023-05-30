## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    // Update NormalizeStatements to return the modified AST so that it can be re-used
    // by later passes that may depend on it.
    NormalizeStatements norm = new NormalizeStatements(compiler, assertOnChange);
    NodeTraversal.traverse(compiler, root, norm);
    Node transformedRoot = norm.getResult();

    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseWithScope(transformedRoot, Scope.createGlobalScope(transformedRoot));
    }

    // Update removeDuplicateDeclarations to use the transformed root instead of root
    removeDuplicateDeclarations(externs, transformedRoot);

    new PropagateConstantAnnotationsOverVars(compiler, assertOnChange).process(externs, transformedRoot);

    if (!compiler.getLifeCycleStage().isNormalized()) {
        compiler.setLifeCycleStage(LifeCycleStage.NORMALIZED);
    }
}

## Fixed Function 2
private void createSynthesizedExternVar(String varName) {
    Node nameNode = IR.name(varName);
    // Mark the variable as constant if it matches the coding convention
    // for constant vars.
    // NOTE(nicksantos): honestly, i'm not sure how much this matters.
    // AFAIK, all people who use the CONST coding convention also
    // compile with undeclaredVars as errors. We have some test
    // cases for this configuration though, and it makes them happier.
    if (compiler.getCodingConvention().isConstant(varName)) {
        nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    }
    Node varNode = IR.var(nameNode);
    getSynthesizedExternsRoot().addChildToBack(varNode);
    varsToDeclareInExterns.remove(varName);
}