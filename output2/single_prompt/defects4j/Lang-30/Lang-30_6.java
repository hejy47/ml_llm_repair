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
                return i;
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
                if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
                    // missing low surrogate, fine, like String.indexOf(String)
                    if (searchChars[j + 1] == cs.charAt(i + 1) && Character.isLowSurrogate(cs.charAt(i + 1))) {
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
            if (Character.isHighSurrogate(ch)) {
                if (j < searchLen - 1 && searchChars[j + 1] == cs.charAt(i + 1) && Character.isLowSurrogate(cs.charAt(i + 1))) {
                    continue outer;
                }
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
            if (Character.isHighSurrogate(ch)) {
                if (i < strLen - 1 && searchChars.indexOf(str.charAt(i + 1)) >= 0 && Character.isLowSurrogate(str.charAt(i + 1))) {
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
    for (int i = 0; i < csLen; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLen; j++) {
            if (searchChars[j] == ch) {
                if (Character.isHighSurrogate(ch)) {
                    if (j < searchLen - 1 && searchChars[j + 1] == cs.charAt(i + 1) && Character.isLowSurrogate(cs.charAt(i + 1))) {
                        continue;
                    }
                }
                // ch is in the Basic Multilingual Plane
                return false;
            }
        }
    }
    return true;
}