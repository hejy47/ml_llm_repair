## Fixed Function 1
static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0;
    }
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}