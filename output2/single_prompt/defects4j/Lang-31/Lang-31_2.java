## Fixed Function 1
// ContainsAny
//-----------------------------------------------------------------------
public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (cs == null || searchChars == null) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    if (csLength == 0 || searchLength == 0) {
        return false;
    }
    if (cs instanceof String) {
        for (int i = 0; i < csLength; i++) {
            char ch = cs.charAt(i);
            if (CharUtils.indexOf(searchChars, ch, 0) >= 0) {
                return true;
            }
        }
    } else {
        for (int i = 0; i < csLength; i++) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}