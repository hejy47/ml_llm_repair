## Fixed Function 1
void add(String newcode) {
    maybeEndStatement();
    if (newcode.isEmpty()) {
        return;
    }
    char c = newcode.charAt(0);
    if ((isWordChar(c) || c == '\\') && isWordChar(getLastChar())) {
        // need space to separate. This is not pretty printing.
        // For example: "return foo;"
        append(" ");
    }
    // Check if newcode ends with a slash and the last code ends with a DIV
    // If so, print newcode without a space in-between
    if (getLastChar() == '/' && newcode.charAt(newcode.length() - 1) == '/') {
        append(newcode);
    } else {
        append(" ");
        append(newcode);
    }
}