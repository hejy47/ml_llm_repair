# Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, this);
    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseRoots(externs, root);
    }
    removeDuplicateDeclarations(root, compiler);
    new PropogateConstantAnnotations(compiler, assertOnChange).process(externs, root);
} 

// Added parameter compiler to removeDuplicateDeclarations to fix the bug.