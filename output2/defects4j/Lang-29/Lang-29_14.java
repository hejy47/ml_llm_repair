## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    return toVersionInt(versionArray);
}