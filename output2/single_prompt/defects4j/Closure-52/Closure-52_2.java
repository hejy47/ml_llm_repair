## Fixed Function 1
static boolean isSimpleNumber(String s) {
    int len = s.length();
    if(len == 0) return false; // added this line to return false if string is empty
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true; // changed return statement to true since all characters are digits
}