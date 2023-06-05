## Fixed Function 1
public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = input.length();
    while (pos < len) {
        // Use Character.codePointAt() method to get the code point at the current position
        int codePoint = Character.codePointAt(input, pos);
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            // Write the code point to the output stream if it was not translated
            out.write(codePoint);
            pos += Character.charCount(codePoint);
        } else {
            // Otherwise, calculate the number of code units consumed and update the position accordingly
            pos += consumed;
        }
    }
}