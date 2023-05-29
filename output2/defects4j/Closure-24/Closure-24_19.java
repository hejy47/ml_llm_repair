## Fixed Function 1
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent.isVar() && !v.isBleedingFunction()) {
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getString();
                Var aliasVar = scope.getVar(name);
                if (aliasVar != null && aliasVar.getInitialValue() != null && aliasVar.getInitialValue().getQualifiedName() != null) {
                    aliases.put(name, aliasVar);
                    String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                    transformation.addAlias(name, qualifiedName);
                }
            } else {
                if (!v.isParam()) {
                    report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
                }
            }
        }
    }
}