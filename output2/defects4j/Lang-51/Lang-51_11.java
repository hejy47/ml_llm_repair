## Fixed Function 1
// String to boolean methods
//-----------------------------------------------------------------------
public static boolean toBoolean(String str) {
    // Use equals() instead of == for comparing strings
    // Use Boolean.parseBoolean() for performance improvement
    // Simplify the case statements
    if (str == null) {
        return false;
    }
    String lowerCase = str.toLowerCase();
    switch(lowerCase) {
        case "true":
        case "yes":
        case "on":
            return true;
        default:
            return Boolean.parseBoolean(lowerCase);
    }
}