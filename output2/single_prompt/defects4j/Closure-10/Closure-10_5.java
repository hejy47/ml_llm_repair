## Fixed Function 1
static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n, false); // add false parameter to avoid infinite recursion
    }
}

// add helper function with additional parameter
private static boolean mayBeStringHelper(Node n, boolean visited) {
    if (visited) {
        return false;
    }
    if (n.isString()) {
        return true;
    }
    if (n.isFunction()) {
        for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
            if (mayBeStringHelper(c, true)) {
                return true;
            }
        }
    }
    return false;
}