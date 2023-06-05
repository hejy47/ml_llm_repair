public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = true; // Set to true to allow a leading sign
    boolean foundDigit = false;
    int start = 0;
    if (sz > 0 && (chars[0] == '-' || chars[0] == '+')) {
        // Set start to 1 for a leading sign
        start = 1;
    }
    // Don't want to loop to the last char in case there is a sign at the end
    sz--;
    int i = start;
    // Loop through each character and validate if it is a number
    while (i <= sz) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                // Two decimal points or a decimal point in exponent
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            if (hasExp) {
                // Two E's in number
                return false;
            }
            if (!foundDigit) {
                // Need to have at least one digit before exponent
                return false;
            }
            hasExp = true;
            allowSigns = true; // Allow sign after exponent
            foundDigit = false; // Need a digit after the exponent
        } else if (chars[i] == '-' || chars[i] == '+') {
            if (!allowSigns) {
                // Sign not in expected position
                return false;
            }
            allowSigns = false; // Only allow if it's immediately after E
            foundDigit = false;
        } else {
            // Invalid character found
            return false;
        }
        i++;
    }
    if (i < chars.length) {
        if (chars[i] == 'f' || chars[i] == 'F' || chars[i] == 'd' || chars[i] == 'D') {
            // Check for valid type qualifier
            i++;
        }
        if (i < chars.length) {
            return false; // Extra characters at end of number
        }
    }
    return foundDigit;
}