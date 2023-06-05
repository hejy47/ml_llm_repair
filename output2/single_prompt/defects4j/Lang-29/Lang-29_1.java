## Fixed Function 1
static float toJavaVersionInt(String version) {
    String[] versionParts = version.split("\\.");
    int major = 0;
    int minor = 0;
    int patch = 0;
    if (versionParts.length > 0) {
        major = Integer.parseInt(versionParts[0]);
    }
    if (versionParts.length > 1) {
        minor = Integer.parseInt(versionParts[1]);
    }
    if (versionParts.length > 2) {
        patch = Integer.parseInt(versionParts[2]);
    }
    return Float.parseFloat(major + "." + minor + patch);
}