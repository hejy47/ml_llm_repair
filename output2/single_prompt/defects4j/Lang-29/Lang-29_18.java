## Fixed Function 1
static float toJavaVersionInt(String version) {
    if(version.trim().isEmpty()) {
        return 0;
    }
    return toVersionInt(toJavaVersionIntArray(version.trim(), JAVA_VERSION_TRIM_SIZE));
}