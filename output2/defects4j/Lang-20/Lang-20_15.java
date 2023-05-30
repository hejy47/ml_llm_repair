## Fixed Function 1
public static String join(Object[] array, char separator, int startIndex, int endIndex) {
    if (array == null || array.length == 0) { // check for empty array
        return "";
    }
    int noOfItems = (endIndex - startIndex);
    if (noOfItems <= 0) {
        return EMPTY;
    }
    StringBuilder buf = new StringBuilder((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
    for (int i = startIndex; i < endIndex; i++) {
        if (i > startIndex && buf.length()>0) { // check if separator should be added
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
    if (array == null || array.length == 0) { // check for empty array
        return "";
    }
    if (separator == null) {
        separator = EMPTY;
    }
    // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
    //           (Assuming that all Strings are roughly equally long)
    int noOfItems = (endIndex - startIndex);
    if (noOfItems <= 0) {
        return EMPTY;
    }
    StringBuilder buf = new StringBuilder((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());
    for (int i = startIndex; i < endIndex; i++) {
        if (i > startIndex && buf.length()>0) { // check if separator should be added
            buf.append(separator);
        }
        if (array[i] != null) {
            buf.append(array[i]);
        }
    }
    return buf.toString();
}