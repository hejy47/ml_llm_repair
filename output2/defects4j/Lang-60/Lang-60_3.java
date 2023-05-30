## Fixed Function 1
public boolean contains(char ch) {
    char[] thisBuf = buffer;
    for (int i = 0; i < size; i++) { //iterate to size, not length
        if (thisBuf[i] == ch) {
            return true;
        }
    }
    return false;
}

## Fixed Function 2
public int indexOf(char ch, int startIndex) {
    startIndex = Math.max(0, startIndex); //use Math.max instead of ternary expression
    if (startIndex >= size) {
        return -1;
    }
    char[] thisBuf = buffer;
    for (int i = startIndex; i < size; i++) { //iterate to size, not length
        if (thisBuf[i] == ch) {
            return i;
        }
    }
    return -1;
}