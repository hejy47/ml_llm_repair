## Fixed Function 1
//-----------------------------------------------------------------------
public boolean contains(char ch) {
    char[] thisBuf = buffer;
    for (int i = 0; i < size; i++) { // Changed to loop until size of buffer instead of length of buffer
        if (thisBuf[i] == ch) {
            return true;
        }
    }
    return false;
}

## Fixed Function 2
public int indexOf(char ch, int startIndex) {
    startIndex = Math.max(0, startIndex); // Changed to use Math.max to ensure startIndex is not negative
    if (startIndex >= size) {
        return -1;
    }
    char[] thisBuf = buffer;
    for (int i = startIndex; i < size; i++) { // Changed to loop until size of buffer instead of length of buffer
        if (thisBuf[i] == ch) {
            return i;
        }
    }
    return -1;
}