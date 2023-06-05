## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    int versionInt = 0;
    for (int i = 0; i < versionArray.length; i++) {
        versionInt = versionInt * 100 + versionArray[i];
    }
    return (float) versionInt;
}