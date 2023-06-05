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
            int c = Character.codePointAt(input, pos);
            out.write(c);
            pos += Character.charCount(c);
        } else {
            // translators understand code points, so skip whole character(s)
            pos += consumed;
        }
    }
}