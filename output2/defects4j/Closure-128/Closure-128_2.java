## Fixed Function 1
static boolean isSimpleNumber(String s) {
    int len = s.length();
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return len > 0 && !(len > 1 && s.charAt(0) == '0');
}

Explanation: The bug in the original function is that it allows strings that start with '0' and have a length greater than 1 to be considered as simple numbers. The fix is to update the check to exclude such strings.