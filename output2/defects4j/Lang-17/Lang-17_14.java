## Fixed Function 1
public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = input.length(); // Use length() instead of codePointCount()
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            char c = input.charAt(pos); // Use charAt() instead of converting to code point
            out.write(c);
        } else {
            for (int pt = 0; pt < consumed; pt++) {
                if (pos < len - 2 && Character.isHighSurrogate(input.charAt(pos)) && Character.isLowSurrogate(input.charAt(pos + 1))) {
                    pos += 2; // Handle surrogate pairs correctly
                } else {
                    pos++;
                }
            }
            pos--;
        }
        pos++;
    }
}