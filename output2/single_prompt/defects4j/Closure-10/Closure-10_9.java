## Fixed Function 1
static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return !n.getType().equals(Token.NAME) && mayBeStringHelper(n);
    }
}