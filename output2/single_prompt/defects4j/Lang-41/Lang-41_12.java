## Fixed Function 1
public static String getShortClassName(String className) {
    if (className == null || className.isEmpty()) {
        return StringUtils.EMPTY;
    }
    // Handle array encoding
    // Strip Object type encoding
    int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
    String out;
    if (innerIdx != -1) {
        out = className.substring(lastDotIdx + 1, innerIdx).replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
    } else {
        out = className.substring(lastDotIdx + 1);
    }   
    return out;
}

## Fixed Function 2
public static String getPackageName(String className) {
    if (className == null || className.isEmpty()) {
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