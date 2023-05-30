## Fixed Function 1
//-----------------------------------------------------------------------
public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }
    // if the lower value is greater than the length of the string,
    // set to the length of the string
    // if the upper value is -1 (i.e. no limit) or is greater
    // than the length of the string, set to the length of the string
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    // if upper is less than lower, raise it to lower
    if (upper < lower) {
        upper = lower;
    }
    // handle the case where lower is 0
    if (lower == 0) {
        int index = StringUtils.indexOf(str, " ", upper);
        if (index == -1) {
            return str.substring(0, upper) + StringUtils.defaultString(appendToEnd);
        } else {
            return str.substring(0, index) + StringUtils.defaultString(appendToEnd);
        }
    }
    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1 || index > upper) {
        result.append(str.substring(lower, upper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        result.append(str.substring(lower, index));
        result.append(StringUtils.defaultString(appendToEnd));
    }
    return result.toString();
}