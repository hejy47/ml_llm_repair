## Fixed Function 1
static boolean isSimpleNumber(String s) {
    int len = s.length();
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return len > 0 && !s.equals("0"); // added condition to check if input string is not "0"
}