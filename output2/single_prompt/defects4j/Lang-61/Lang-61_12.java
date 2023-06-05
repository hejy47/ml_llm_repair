## Fixed Function 1
public int indexOf(String str, int startIndex) {
    startIndex = Math.max(startIndex, 0); // Use Math.max to ensure startIndex is at least 0
    if (str == null || startIndex >= size()) {
        return -1; // Use size() instead of size
    }
    int strLen = str.length();
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex);
    }
    if (strLen == 0) {
        return startIndex;
    }
    if (strLen > size() - startIndex) {
        return -1; // Check if str can fit in the remaining portion of the buffer
    }
    char[] thisBuf = buffer;
    int len = thisBuf.length - strLen;
    outer: for (int i = startIndex; i <= len; i++) { // Change < to <=
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}