## Fixed Function 1
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getToken().ordinal();
        Node parent = n.getParent();
        if (parent.isVar()) {
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getString();
                Var aliasVar = scope.getVar(name);
                if (aliasVar == null) {
                    // If there is no aliasVar, it means the variable is not defined in the current
                    // scope. Don't try to add it to the aliases map or get its qualified name.
                    continue;
                }
                aliases.put(name, aliasVar);
                Node initialValue = aliasVar.getInitialValue();
                if (initialValue != null && initialValue.isQualifiedName()) {
                    String qualifiedName = initialValue.getQualifiedName();
                    transformation.addAlias(name, qualifiedName);
                }
                // Bleeding functions already get a BAD_PARAMETERS error, so just
                // do nothing.
                // Parameters of the scope function also get a BAD_PARAMETERS
                // error.
            } else {
                // TODO(robbyw): Support using locals for private variables.
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        }
    }
}