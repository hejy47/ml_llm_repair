## Fixed Function 1
static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n, new HashSet<Node>());
    }
}

static boolean mayBeStringHelper(Node n, HashSet<Node> visited) {
    if (visited.contains(n)) {
        return false;
    }
    visited.add(n);
    if (n.getType() == Token.STRING || n.getType() == Token.TEMPLATELIT) {
        return true;
    }
    for (int i = 0, count = n.getChildCount(); i < count; i++) {
        if (mayBeStringHelper(n.getChildAtIndex(i), visited)) {
            return true;
        }
    }
    return false;
}