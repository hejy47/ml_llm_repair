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
    String varName = lhs.getString();
    Var var = s.getVar(varName);
    return var != null && var.getScope() == s && !blacklistedVars.contains(varName);
}