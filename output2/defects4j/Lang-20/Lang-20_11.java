## Fixed Function 1
public static String join(Object[] array, char separator, int startIndex, int endIndex) {
    if (array == null) {
        return null;
    }
    int noOfItems = (endIndex - startIndex);
    if (noOfItems <= 0) {
        return EMPTY;
    }
    StringBuilder buf = new StringBuilder((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + noOfItems - 1);
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
    if (array == null) {
        return null;
    }
    if (separator == null) {
        separator = EMPTY;
    }
    int noOfItems = (endIndex - startIndex);
    if (noOfItems <= 0) {
        return EMPTY;
    }
    int totalLen = 0;
    for (int i = startIndex; i < endIndex; i++) {
        if (array[i] != null) {
            totalLen += array[i].toString().length();
        }
    }
    totalLen += ((noOfItems - 1) * separator.length());
    StringBuilder buf = new StringBuilder(totalLen);
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