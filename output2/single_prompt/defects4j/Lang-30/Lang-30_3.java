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
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                return true;
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
                return false;
            }
        }
    }
    return true;
}