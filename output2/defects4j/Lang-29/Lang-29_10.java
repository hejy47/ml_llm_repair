## Fixed Function 1
static float toJavaVersionInt(String version) {
    try {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
    } catch (IllegalArgumentException e) {
        return 0.0f;
    }
}