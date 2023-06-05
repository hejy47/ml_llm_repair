## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    new NodeTraversal(compiler, new NormalizeStatements(compiler, assertOnChange)).traverse(root);
    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseRoots(externs, root);
    }
    // It is important that removeDuplicateDeclarations runs after
    // MakeDeclaredNamesUnique in order for catch block exception names to be
    // handled properly. Specifically, catch block exception names are
    // only valid within the catch block, but our current Scope logic
    // has no concept of this and includes it in the containing function
    // (or global scope). MakeDeclaredNamesUnique makes the catch exception
    // names unique so that removeDuplicateDeclarations() will properly handle
    // cases where a function scope variable conflict with a exception name:
    //   function f() {
    //      try {throw 0;} catch(e) {e; /* catch scope 'e'*/}
    //      var e = 1; // f scope 'e'
    //   }
    // otherwise 'var e = 1' would be rewritten as 'e = 1'.
    // TODO(johnlenz): Introduce a separate scope for catch nodes.
    // We needed to add "root" to the call to removeDuplicateDeclarations
    // for the fix as it is called on the scope created for `root`
    removeDuplicateDeclarations(externs, root, root);
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
    // We updated the code to use IR.var to create a VAR node which properly contains the NAME node.
    Node varNode = IR.var(nameNode);
    if (compiler.getCodingConvention().isConstant(varName)) {
        nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    }
    getSynthesizedExternsRoot().addChildToBack(varNode);
    varsToDeclareInExterns.remove(varName);
}