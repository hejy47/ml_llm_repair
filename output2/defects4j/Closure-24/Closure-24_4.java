## Fixed Function 1
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent.isVar() && n.hasChildren() && n.getFirstChild().isQualifiedName()) {
            String name = n.getString();
            Var aliasVar = scope.getVar(name);
            if (aliasVar != null && aliasVar.isAlias()) {
                aliases.put(name, aliasVar);
                String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                transformation.addAlias(name, qualifiedName);
            }
        }
    }
}