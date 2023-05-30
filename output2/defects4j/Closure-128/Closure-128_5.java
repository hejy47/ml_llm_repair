## Fixed Function 1
static boolean isSimpleNumber(String s) {
    int len = s.length();
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return len > 0 && s.charAt(0) != '0';
}

The fix for this function is to use the `Character.isDigit()` method to check if a character is a digit instead of manually checking if it falls within the ASCII range of '0' to '9'.