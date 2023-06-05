public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;
    // deal with any possible sign up front
    int start = (chars[0] == '-') ? 1 : 0;
    if (sz > start + 1) {
        if (chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                // str == "0x"
                return false;
            }
            // checking hex (it can't be anything else)
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
    }
    // don't want to loop to the last char, check it afterwards
    sz--;
    // loop to the next to the last char or to the last char if we need another digit to
    // make a valid number (e.g. chars[0..5] = "1234E")
    int i = start;
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        char c = chars[i];
        if (c >= '0' && c <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (c == '.') {
            if (hasDecPoint || hasExp) {
                // two decimal points or dec in exponent
                return false;
            }
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            // we've already taken care of hex.
            if (hasExp) {
                // two E's
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (c == '+' || c == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            // we need a digit after the E
            foundDigit = false;
        } else {
            return false;
        }
        i++;
    }
    char c = chars[i];
    if (c >= '0' && c <= '9') {
        // no type qualifier, OK
        return true;
    }
    if (c == 'e' || c == 'E') {
        // can't have an E at the last byte
        return false;
    }
    if (c == '.') {
        if (hasDecPoint || hasExp) {
            // two decimal points or dec in exponent
            return false;
        }
        // single trailing decimal point after non-exponent is ok
        return foundDigit;
    }
    if (!allowSigns && (c == 'd' || c == 'D' || c == 'f' || c == 'F')) {
        return foundDigit;
    }
    if (c == 'l' || c == 'L') {
        // not allowing L with an exponent or decimal point
        return foundDigit && !hasExp && !hasDecPoint;
    }
    // last character is illegal
    return false;
}