## Fixed Function 1
static boolean isSimpleNumber(String s) {
    if(s.isEmpty()) {
        return false;
    }
    int len = s.length();
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true;
}