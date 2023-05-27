## Fixed Function 1
static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n.getChildNodes(), MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n);
    }
}