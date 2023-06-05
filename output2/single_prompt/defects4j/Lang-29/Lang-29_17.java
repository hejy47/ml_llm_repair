## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    float versionInt = 0.0f;
    for (int i = 0; i < versionArray.length; i++) {
        versionInt += versionArray[i] * Math.pow(0.1, i);
    }
    return versionInt;
}