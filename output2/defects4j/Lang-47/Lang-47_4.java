## Fixed Function 1
public StrBuilder appendFixedWidthPadLeft(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(strLen - width, strLen, buffer, size);
        } else {
            int padLen = width - strLen;
            int index = size;
            for (int i = 0; i < padLen; i++) {
                buffer[index++] = padChar;
            }
            str.getChars(0, strLen, buffer, index);
        }
        size += width;
    }
    return this;
}

## Fixed Function 2
public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
    if (width > 0) {
        ensureCapacity(size + width);
        String str = (obj == null ? getNullText() : obj.toString());
        int strLen = str.length();
        if (strLen >= width) {
            str.getChars(0, width, buffer, size);
        } else {
            int padLen = width - strLen;
            str.getChars(0, strLen, buffer, size);
            int index = size + strLen;
            for (int i = 0; i < padLen; i++) {
                buffer[index++] = padChar;
            }
        }
        size += width;
    }
    return this;
}