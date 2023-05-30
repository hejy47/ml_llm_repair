## Fixed Function 1
// IndexOfAny chars
//-----------------------------------------------------------------------
public static int indexOfAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return INDEX_NOT_FOUND;
    }
    int csLen = cs.length();
    int searchLen = searchChars.length;
    for (int i = 0; i < csLen; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLen; j++) {
            if (searchChars[j] == ch) {
                // ch is a supplementary character
                if (Character.isHighSurrogate(ch)) {
                    // Missing low surrogate
                    if (j + 1 == searchLen) {
                        return i;
                    }
                    // Check if the next char is a low surrogate
                    if (Character.isLowSurrogate(searchChars[j + 1])) {
                        if (i + 1 < csLen && cs.charAt(i + 1) == Character.toCodePoint(ch, searchChars[j + 1])) {
                            return i;
                        }
                    }
                } else {
                    return i;
                }
            }
        }
    }
    return INDEX_NOT_FOUND;
}

## Fixed Function 2
// ContainsAny
//-----------------------------------------------------------------------
public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    int csLast = csLength - 1;
    int searchLast = searchLength - 1;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                if (Character.isHighSurrogate(ch)) {
                    if (j == searchLast) {
                        return true;
                    }
                    // Missing low surrogate
                    if (Character.isLowSurrogate(searchChars[j + 1])) {
                        if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }
    }
    return false;
}

## Fixed Function 3
public static boolean containsAny(CharSequence cs, String searchChars) {
    if (searchChars == null) {
        return false;
    }
    return containsAny(cs, searchChars.toCharArray());
}

## Fixed Function 4
// IndexOfAnyBut chars
//-----------------------------------------------------------------------
public static int indexOfAnyBut(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return INDEX_NOT_FOUND;
    }
    int csLen = cs.length();
    int searchLen = searchChars.length;
    for (int i = 0; i < csLen; i++) {
        char ch = cs.charAt(i);
        boolean chFound = false;
        for (int j = 0; j < searchLen; j++) {
            if (searchChars[j] == ch) {
                chFound = true;
                break;
            }
            if (Character.isHighSurrogate(searchChars[j])) {
                if (j + 1 < searchLen && Character.isLowSurrogate(searchChars[j + 1])) {
                    if (i < csLen - 1 && searchChars[j + 1] == cs.charAt(i + 1)) {
                        chFound = true;
                        break;
                    }
                }
            }
        }
        if (!chFound) {
            return i;
        }
    }
    return INDEX_NOT_FOUND;
}

## Fixed Function 5
public static int indexOfAnyBut(String str, String searchChars) {
    if (isEmpty(str) || isEmpty(searchChars)) {
        return INDEX_NOT_FOUND;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
        char ch = str.charAt(i);
        if (searchChars.indexOf(ch) < 0) {
            if (!Character.isHighSurrogate(ch)) {
                return i;
            }
            if (i < strLen - 1) {
                char low = str.charAt(i + 1);
                if (Character.isLowSurrogate(low)) {
                    i++;
                }
            }
        }
    }
    return INDEX_NOT_FOUND;
}

## Fixed Function 6
// ContainsNone
//-----------------------------------------------------------------------
public static boolean containsNone(CharSequence cs, char[] searchChars) {
    if (cs == null || searchChars == null) {
        return true;
    }
    int csLen = cs.length();
    int searchLen = searchChars.length;
    for (int i = 0; i < csLen; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLen; j++) {
            if (searchChars[j] == ch) {
                if (Character.isHighSurrogate(ch)) {
                    if (j == searchLen - 1) {
                        return true;
                    }
                    // Missing low surrogate
                    if (Character.isLowSurrogate(searchChars[j + 1])) {
                        if (i < csLen - 1 && searchChars[j + 1] == cs.charAt(i + 1)) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
    }
    return true;
}