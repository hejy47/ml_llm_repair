## Fixed Function 1
void add(String newcode) {
    maybeEndStatement();
    if (newcode.length() == 0) {
        return;
    }
    char c = newcode.charAt(0);
    if ((isWordChar(c) || c == '\\') && isWordChar(getLastChar())) {
        // need space to separate. This is not pretty printing.
        // For example: "return foo;"
        append(" ");
    }
    // check for DIV followed by REGEXP
    if (getLastType() == TokenType.DIV && c == '/' && getLastChar() == '/') {
        append(" ");
    }
    append(newcode);
}