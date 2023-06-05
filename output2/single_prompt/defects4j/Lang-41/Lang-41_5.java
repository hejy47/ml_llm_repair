## Fixed Function 1
public static String getShortClassName(String className) {
    if (className == null || className.isEmpty()) { // check for empty string with .isEmpty() method
        return StringUtils.EMPTY;
    }
    // Handle array encoding
    // Strip Object type encoding
    int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    int innerIdx = className.lastIndexOf(INNER_CLASS_SEPARATOR_CHAR); // changed 'indexOf' to 'lastIndexOf' to handle multiple inner classes
    String out = className.substring(lastDotIdx + 1);
    if (innerIdx != -1) {
        out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
    }
    return out;
}

## Fixed Function 2
public static String getPackageName(String className) {
    if (className == null || className.isEmpty()) { // check for empty string with .isEmpty() method
        return StringUtils.EMPTY;
    }
    // Strip array encoding
    // Strip Object type encoding
    int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (i == -1) {
        return StringUtils.EMPTY;
    }
    return className.substring(0, i);
}