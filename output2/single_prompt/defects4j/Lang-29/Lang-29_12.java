## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    float versionFloat = toVersionInt(versionArray[0], versionArray[1], versionArray[2]);
    return versionFloat;
}