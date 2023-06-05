## Fixed Function 1
public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = Character.codePointCount(input, 0, input.length());
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            int codePoint = Character.codePointAt(input, pos);
            if (codePoint > Character.MAX_VALUE) {
                out.write(Character.toChars(codePoint));
            } else {
                out.write((char) codePoint);
            }
        } else {
            // translators understand codepoints and have taken care of surrogate pairs
            for (int pt = 0; pt < consumed; pt++) {
                if (pos < len - 2) {
                    pos += Character.charCount(Character.codePointAt(input, pos));
                } else {
                    pos++;
                }
            }
            pos--;
        }
        pos++;
    }
}