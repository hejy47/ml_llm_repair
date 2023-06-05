## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] versionArr = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    int major = versionArr[0];
    int minor = versionArr.length > 1 ? versionArr[1] : 0;
    int patch = versionArr.length > 2 ? versionArr[2] : 0;
    return toVersionInt(new int[]{major, minor, patch});
}