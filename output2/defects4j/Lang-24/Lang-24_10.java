public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = true; // Fix #1: allow signs at the beginning of the string
    boolean foundDigit = false;
    int start = 0;
    if (chars[0] == '-') {
        start = 1;
    }
    if (sz > start + 1) {
        if (chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false;
            }
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
    }
    sz--;
    int i = start;
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) {
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false;
        } else {
            return false;
        }
        i++;
    }
    if (i < chars.length) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            return true;
        }
        if (chars[i] == 'e' || chars[i] == 'E') {
            return false;
        }
        if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                return false;
            }
            return foundDigit;
        }
        if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
            return foundDigit;
        }
        if (chars[i] == 'l' || chars[i] == 'L') {
            return foundDigit && !hasExp && !hasDecPoint; // Fix #2: disallow 'l' qualifier when there's a decimal point or exponent
        }
        return false;
    }
    return !allowSigns && foundDigit;
}