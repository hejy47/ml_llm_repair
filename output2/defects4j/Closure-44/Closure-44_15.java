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
        // Do not allow a forward slash to appear after a DIV.
        // For example,
        // REGEXP DIV REGEXP
        // is valid and should print like
        // / // / /
        if(c == '/' && getLastChar() == '/') {
            append(" ");
        }
    }
    append(newcode);
}