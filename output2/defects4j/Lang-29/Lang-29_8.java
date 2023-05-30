## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionIntArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    if (versionIntArray.length > 0) {
        return toVersionInt(versionIntArray);
    } else {
        return -1; // or throw an exception, depending on the desired behavior
    }
}