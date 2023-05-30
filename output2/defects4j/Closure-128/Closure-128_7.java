## Fixed Function 1
static boolean isSimpleNumber(String s) {
    if (s == null || s.length() == 0) {
        return false;
    }
    for (int index = 0; index < s.length(); index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true;
}