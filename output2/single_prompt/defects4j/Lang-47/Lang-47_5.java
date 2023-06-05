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
            for (int i = 0; i < padLen; i++) {
                buffer[size + i] = padChar;
            }
            str.getChars(0, strLen, buffer, size + padLen);
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
            size += strLen; // Add this line to move the size pointer to after the appended string
            for (int i = 0; i < padLen; i++) {
                buffer[size + i] = padChar;
            }
        }
        size += padLen; // Update the size pointer after padding
    }
    return this;
}