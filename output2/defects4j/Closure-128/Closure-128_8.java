## Fixed Function 1
static boolean isSimpleNumber(String s) {
    int len = s.length();
    if(len == 0 || s.charAt(0) == '0')
        return false;
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true;
}