## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionIntArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    if (versionIntArray == null) {
        return -1;
    }
    return toVersionInt(versionIntArray);
}