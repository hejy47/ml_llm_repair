## Fixed Function 1
static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n, new HashSet<>());
    }
}

static boolean mayBeStringHelper(Node n, HashSet<Node> visited) {
    if (n == null || visited.contains(n)) {
        return false;
    }
    visited.add(n);
    if (n.isString()) {
        return true;
    }
    if (n.isCall()) {
        for (Node arg : n.getChildNodes()) {
            if (mayBeStringHelper(arg, visited)) {
                return true;
            }
        }
    }
    if (n.isNew()) {
        for (Node arg : n.getChildNodes()) {
            if (mayBeStringHelper(arg, visited)) {
                return true;
            }
        }
    }
    return false;
}