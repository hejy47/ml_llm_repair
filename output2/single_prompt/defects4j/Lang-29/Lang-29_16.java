## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionIntArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    float versionInt = 0;
    for (int i = 0; i < versionIntArray.length; i++) {
        versionInt += versionIntArray[i] * Math.pow(0.1, i + 2);
    }
    return versionInt;
}