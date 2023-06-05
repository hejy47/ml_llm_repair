## Fixed Function 1
public int indexOf(String str, int startIndex) {
    startIndex = Math.max(startIndex, 0); // Fix: Use Math.max to ensure startIndex is at least 0
    if (str == null || startIndex >= size()) { // Fix: Use size() method instead of size field
        return -1;
    }
    int strLen = str.length();
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex);
    }
    if (strLen == 0) {
        return startIndex;
    }
    if (strLen > size() - startIndex) { // Fix: Check if strLen is greater than remaining search space
        return -1;
    }
    char[] thisBuf = buffer;
    int len = thisBuf.length - strLen;
    outer: for (int i = startIndex; i <= len; i++) { // Fix: Include end of buffer in search space
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}