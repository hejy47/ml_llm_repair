## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    int major = versionArray[0];
    int minor = 0;
    if (versionArray.length > 1) {
        minor = versionArray[1];
    }
    return Float.parseFloat(major + "." + minor);
}