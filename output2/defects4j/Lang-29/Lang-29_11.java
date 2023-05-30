## Fixed Function 1
static float toJavaVersionInt(String version) {
    String[] versionArray = version.split("\\.");
    if (versionArray.length > 2) {
        return Float.parseFloat(versionArray[0] + "." + versionArray[1]);
    }
    return Float.parseFloat(versionArray[0]);
}