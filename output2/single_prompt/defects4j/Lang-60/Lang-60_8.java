## Fixed Function 1
//-----------------------------------------------------------------------
public boolean contains(char ch) {
    char[] thisBuf = buffer;
    for (int i = 0; i < size; i++) { // we only need to iterate over actual data in buffer, not the whole buffer
        if (thisBuf[i] == ch) {
            return true;
        }
    }
    return false;
}

## Fixed Function 2
public int indexOf(char ch, int startIndex) {
    startIndex = (startIndex < 0 ? 0 : startIndex);
    if (startIndex >= size) {
        return -1;
    }
    char[] thisBuf = buffer;
    for (int i = startIndex; i < size; i++) { // we only need to iterate over actual data in buffer, not the whole buffer
        if (thisBuf[i] == ch) {
            return i;
        }
    }
    return -1;
}