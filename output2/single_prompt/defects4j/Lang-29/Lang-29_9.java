## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionIntArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    if (versionIntArray.length < 2) {
        return (float) versionIntArray[0];
    }
    return (float) versionIntArray[0] + ((float) versionIntArray[1] / 10);
}