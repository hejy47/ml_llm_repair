## Fixed Function 1
static float toJavaVersionInt(String version) {
    try {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
    } catch (NumberFormatException e) {
        return 0;
    }
}