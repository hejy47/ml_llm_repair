## Fixed Function 1
static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else if (n != null) {
        return mayBeStringHelper(n);
    }
    return false;
}