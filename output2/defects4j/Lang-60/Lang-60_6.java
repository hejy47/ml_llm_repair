## Fixed Function 1
//-----------------------------------------------------------------------
public boolean contains(char ch) {
    char[] thisBuf = buffer;
    for (int i = 0; i < size(); i++) {
        if (thisBuf[i] == ch) {
            return true;
        }
    }
    return false;
}

## Fixed Function 2
public int indexOf(char ch, int startIndex) {
    startIndex = Math.min(startIndex, size() - 1);
    if (startIndex < 0) {
        return -1;
    }
    char[] thisBuf = buffer;
    for (int i = startIndex; i < size(); i++) {
        if (thisBuf[i] == ch) {
            return i;
        }
    }
    return -1;
}