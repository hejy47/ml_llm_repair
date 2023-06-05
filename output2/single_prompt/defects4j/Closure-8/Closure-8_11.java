## Fixed Function 1
private boolean canBeRedeclared(Node n, Scope s) {
    if (!NodeUtil.isExprAssign(n)) {
        return false;
    }
    Node assign = n.getFirstChild();
    Node lhs = assign.getFirstChild();
    if (!lhs.isName()) {
        return false;
    }
    Var var = s.getVar(lhs.getString());
    return var != null && !blacklistedVars.contains(var) && (var.getScope() == s || var.isGlobal());
}