## Fixed Function 1
public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return contains(str.toUpperCase(), searchStr.toUpperCase());
}

## Fixed Function 2
public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return contains(str.toLowerCase(), searchStr.toLowerCase());
}