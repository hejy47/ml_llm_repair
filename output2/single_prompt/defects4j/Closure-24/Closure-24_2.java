## Fixed Function 1
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent != null && parent.isVar()) { // check if parent exists first
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getString();
                Var aliasVar = scope.getVar(name);
                if (aliasVar != null && aliasVar.getInitialValue() != null) { // check if aliasVar and initialValue exist
                    aliases.put(name, aliasVar);
                    String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                    transformation.addAlias(name, qualifiedName);
                }
                else {
                    // handle the case where aliasVar or initialValue is null
                    report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
                }
            } else {
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        }
    }
}