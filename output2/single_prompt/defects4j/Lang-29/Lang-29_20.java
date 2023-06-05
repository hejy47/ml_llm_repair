## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] intArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    float versionInt = 0.0f;
    for (int i = 0; i < intArray.length; i++) {
        versionInt += intArray[i] * Math.pow(10, (intArray.length - i - 1) * 2);
    }
    return versionInt;
}