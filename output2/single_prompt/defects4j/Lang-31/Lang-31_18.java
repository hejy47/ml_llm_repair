## Fixed Function 1
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
        int chInt = Character.codePointAt(cs, i);
        if (Character.isSupplementaryCodePoint(chInt)) {
            i++;
        }
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                // ch is a supplementary character
                // ch is in the Basic Multilingual Plane
                return true;
            }
        }
    }
    return false;
}

Explanation:
The bug in function `containsAny` is that it doesn't handle supplementary characters correctly. When a supplementary character is encountered in the input string, it should increment the index by two, instead of one. This is because supplementary characters are represented by two consecutive `char` values, which together form a code point. The fixed function handles supplementary characters correctly by using the `codePointAt` method from the `Character` class to test for supplementary characters, and incrementing the index accordingly.