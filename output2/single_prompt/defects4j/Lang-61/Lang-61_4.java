## Fixed Function 1
public int indexOf(String str, int startIndex) {
    startIndex = Math.max(startIndex, 0); // fixed to ensure startIndex is zero or positive
    if (str == null || startIndex >= size()) { // fixed to call size() method instead of size field
        return -1;
    }
    int strLen = str.length();
    if (strLen == 1) {
        return indexOf(str.charAt(0), startIndex);
    }
    if (strLen == 0) {
        return startIndex;
    }
    if (strLen > size() - startIndex) { // fixed to ensure strLen does not exceed remaining length of buffer
        return -1;
    }
    char[] thisBuf = buffer;
    int len = size() - strLen + 1; // fixed to calculate correct length
    outer: for (int i = startIndex; i < len; i++) {
        for (int j = 0; j < strLen; j++) {
            if (str.charAt(j) != thisBuf[i + j]) {
                continue outer;
            }
        }
        return i;
    }
    return -1;
}