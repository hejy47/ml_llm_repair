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
    if (innerIdx == -1) {
        return className.substring(lastDotIdx + 1);
    } else {
        return className.substring(lastDotIdx + 1, innerIdx).replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR) + PACKAGE_SEPARATOR_CHAR + className.substring(innerIdx + 1, className.length());
    }
}

## Fixed Function 2
public static String getPackageName(String className) {
    if (className == null) {
        return StringUtils.EMPTY;
    }
    // Strip array encoding
    // Strip Object type encoding
    int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (i == -1) {
        return StringUtils.EMPTY;
    } else {
        return className.substring(0, i);
    }
}