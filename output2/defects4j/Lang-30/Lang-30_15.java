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
                    if (i < csLen - 1 && Character.isLowSurrogate(cs.charAt(i + 1))) {
                        return i;
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
                if (ch >= Character.MIN_HIGH_SURROGATE && ch <= Character.MAX_LOW_SURROGATE) {
                    if (j == searchLast) {
                        // missing low surrogate, fine, like String.indexOf(String)
                        return true;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return true;
                    }
                } else {
                    // ch is in the Basic Multilingual Plane
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
    outer: for (int i = 0; i < csLen; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLen; j++) {
            if (searchChars[j] == ch) {
                continue outer;
            }
        }
        return i;
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
            if (ch >= Character.MIN_HIGH_SURROGATE && ch <= Character.MAX_HIGH_SURROGATE) {
                if (i < strLen - 1 && Character.isLowSurrogate(str.charAt(i + 1))) {
                    continue;
                }
            }
            return i;
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
    outer: for (int i = 0; i < csLen; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLen; j++) {
            if (searchChars[j] == ch) {
                if (ch >= Character.MIN_HIGH_SURROGATE && ch <= Character.MAX_HIGH_SURROGATE) {
                    if (i < csLen - 1 && Character.isLowSurrogate(cs.charAt(i + 1))) {
                        continue outer;
                    }
                }
                return false;
            }
        }
    }
    return true;
}