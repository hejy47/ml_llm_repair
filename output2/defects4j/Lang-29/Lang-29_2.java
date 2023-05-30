## Fixed Function 1
static float toJavaVersionInt(String version) {
    int[] intArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    if (intArray.length < 2) {
        return (float) intArray[0];
    }
    return Float.parseFloat(intArray[0] + "." + intArray[1]);
}