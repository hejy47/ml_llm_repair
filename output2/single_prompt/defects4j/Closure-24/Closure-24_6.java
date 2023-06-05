## Fixed Function 1
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        if (n == null) {
            continue;
        }
        int type = n.getType();

        if (NodeUtil.isNameDeclaration(n.getParent())) {
            Node initialValue = n.getFirstChild(); 
            if (initialValue != null && initialValue.isQualifiedName()) {
                String name = n.getString();
                Var aliasVar = scope.getVar(name);
                aliases.put(name, aliasVar);
                String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                transformation.addAlias(name, qualifiedName);
            } else {
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        }
    }
}