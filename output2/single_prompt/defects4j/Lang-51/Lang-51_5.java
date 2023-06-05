// String to boolean methods
//-----------------------------------------------------------------------
public static boolean toBoolean(String str) {
    // Previously used equalsIgnoreCase, which was fast for interned 'true'.
    // Non interned 'true' matched 15 times slower.
    //
    // Optimisation provides same performance as before for interned 'true'.
    // Similar performance for null, 'false', and other strings not length 2/3/4.
    // 'true'/'TRUE' match 4 times slower, 'tRUE'/'True' 7 times slower.
    if (str == null || str.isEmpty()) {
        return false;
    }
    str = str.trim();
    int len = str.length();
    if (len == 1) {
        char ch = str.charAt(0);
        return (ch == 't' || ch == 'T');
    } else if (len == 4) {
        return (str.equalsIgnoreCase("true"));
    } else if (len == 5) {
        return (str.equalsIgnoreCase("false") == false);
    } else if (len == 3) {
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        char ch2 = str.charAt(2);
        return ((ch0 == 'y' || ch0 == 'Y') && (ch1 == 'e' || ch1 == 'E') && (ch2 == 's' || ch2 == 'S'));
    }
    return false;
}