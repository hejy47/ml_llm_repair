## Fixed Function 1
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    ScopedAliases scopedAliases = new ScopedAliases();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        Node parent = n.getParent();
        boolean isVar = parent.isVar() || parent.isCatch();
        boolean isFunction = parent.isFunction();
        if (isVar && n.getFirstChild() != null && n.getFirstChild().isQualifiedName()) {
            recordAlias(v, scopedAliases);
        } else if (v.isBleedingFunction()) {
            // Bleeding functions already get a BAD_PARAMETERS error, so just
            // do nothing.
        } else if (parent.getType() == Token.LP) {
            // Parameters of the scope function also get a BAD_PARAMETERS
            // error.
        } else if (isVar) {
            Node grandparent = parent.getParent();
            Node value = NodeUtil.getRValueOfLValue(n);
            int nameCount = scopedAliases.countName(v.getName());
            String globalName = "$jscomp.scope." + v.getName() + (nameCount == 0 ? "" : ("$" + nameCount));
            compiler.ensureLibraryInjected("base");
            // First, we need to free up the function expression (EXPR)
            // to be used in another expression.
            // Replace "function NAME() { ... }" with "var NAME;".
            // We can't keep the local name on the function expression,
            // because IE is buggy and will leak the name into the global
            // scope. This is covered in more detail here:
            // http://wiki.ecmascript.org/lib/exe/fetch.php?id=resources:resources&cache=cache&media=resources:jscriptdeviationsfromes3.pdf
            //
            // This will only cause problems if this is a hoisted, recursive
            // function, and the programmer is using the hoisting.
            // If this is a VAR, we can just detach the expression and
            // the tree will still be valid.
            if (value != null || v.getJSDocInfo() != null) {
                Node newDecl = NodeUtil.newQualifiedNameNodeDeclaration(compiler.getCodingConvention(), globalName, value, v.getJSDocInfo()).useSourceInfoIfMissingFromForTree(n);
                NodeUtil.setDebugInformation(newDecl.getFirstChild().getFirstChild(), n, v.getName());
                grandparent.addChildBefore(newDecl, parent);
            }
            // Rewrite "var name = EXPR;" to "var name = $jscomp.scope.name;"
            n.replaceWith(NodeUtil.newQualifiedNameNode(compiler.getCodingConvention(), globalName, n, v.getName()));
            recordAlias(v, scopedAliases);
        } else {
            // Do not other kinds of local symbols, like catch params.
            report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
    }
}

## Fixed Function 2
public Node getChildBefore(Node child) {
    if (child == first) {
        return null;
    }
    Node n = first;
    while (n.getNext() != child) {
        n = n.getNext();
        if (n == null) {
            throw new RuntimeException("node is not a child");
        }
    }
    return n;
}