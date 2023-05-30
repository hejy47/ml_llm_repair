## Fixed Function 1
public static String getShortClassName(String className) {
    if (className == null) {
        return StringUtils.EMPTY;
    }
    if (className.length() == 0) {
        return StringUtils.EMPTY;
    }
    // Handle array encoding
    // Strip Object type encoding
    int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
    String out = className.substring(lastDotIdx + 1);
    if (innerIdx != -1) {
        out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        out = out.substring(0, innerIdx - lastDotIdx - 1) + "$" + out.substring(innerIdx - lastDotIdx); // Fix for inner classes
    }
    return out;
}

## Fixed Function 2
public static String getPackageName(String className) {
    if (className == null) {
        return StringUtils.EMPTY;
    }
    // Strip array encoding
    // Strip Object type encoding
    int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    int endIndex = className.lastIndexOf('$'); // Fix for inner classes
    if (i == -1) {
        return StringUtils.EMPTY;
    }
    int startIndex = className.charAt(0) == '[' ? 1 : 0; // Fix for arrays
    return (endIndex >= startIndex ? className.substring(startIndex, endIndex) : className.substring(startIndex, i));
}