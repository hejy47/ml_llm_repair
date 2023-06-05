## Fixed Function 1
void add(String newcode) {
    maybeEndStatement();
    if (newcode.length() == 0) {
        return;
    }
    char c = newcode.charAt(0);
    if ((isWordChar(c) || c == '\\') && isWordChar(getLastChar())) {
        // need space to separate if not already separated by space or forward slash
        // This is not pretty printing.
        // For example: "return foo;"
        String lastCode = getLastCode();
        if (!lastCode.endsWith(" ") && !lastCode.endsWith("/")) {
            append(" ");
        }
    }
    append(newcode);
}

// Helper function to get the last code added to the output
private String getLastCode() {
    String output = getCode().toString();
    return output.substring(output.lastIndexOf(";") + 1).trim();
}