## Fixed Function 1
public static String getShortClassName(String className) {
    if (className == null || className.isEmpty()) {
        return StringUtils.EMPTY;
    }
    int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR);
    if (innerIdx != -1) {
        className = className.substring(0, innerIdx);
    }
    int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (lastDotIdx != -1) {
        className = className.substring(lastDotIdx + 1);
    }
    return className;
}

## Fixed Function 2
public static String getPackageName(String className) {
    if (className == null || className.isEmpty()) {
        return StringUtils.EMPTY;
    }
    int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (i != -1) {
        return className.substring(0, i);
    }
    return StringUtils.EMPTY;
}