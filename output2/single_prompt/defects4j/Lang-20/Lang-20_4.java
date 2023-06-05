## Fixed Function 1
public static String join(Object[] array, char separator, int startIndex, int endIndex) {
    if (array == null || array.length == 0 || startIndex >= endIndex) {
        return "";
    }
    StringBuilder buf = new StringBuilder();
    for (int i = startIndex; i < endIndex; i++) {
        if (i > startIndex) {
            buf.append(separator);
        }
        if (array[i] != null) {
            buf.append(array[i]);
        }
    }
    return buf.toString();
}

## Fixed Function 2
public static String join(Object[] array, String separator, int startIndex, int endIndex) {
    if (array == null || array.length == 0 || startIndex >= endIndex) {
        return "";
    }
    if (separator == null) {
        separator = "";
    }
    StringBuilder buf = new StringBuilder();
    for (int i = startIndex; i < endIndex; i++) {
        if (i > startIndex) {
            buf.append(separator);
        }
        if (array[i] != null) {
            buf.append(array[i]);
        }
    }
    return buf.toString();
}