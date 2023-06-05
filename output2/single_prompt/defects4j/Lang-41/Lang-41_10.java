## Fixed Function 1
public static String getShortClassName(String className) {
    if (className == null || className.isEmpty()) { // fixed null check and empty check
        return StringUtils.EMPTY;
    }
    int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
    String out = className.substring(lastDotIdx + 1);
    if (innerIdx != -1) {
        out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
    }
    return out;
}

## Fixed Function 2
public static String getPackageName(String className) {
    if (className == null || className.isEmpty()) { // fixed null check and empty check
        return StringUtils.EMPTY;
    }
    int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (i == -1) {
        return StringUtils.EMPTY;
    }
    return className.substring(0, i);
}