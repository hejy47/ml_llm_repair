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
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            int currentCodePoint = Character.codePointAt(input, pos);
            out.write(currentCodePoint);
            pos += Character.charCount(currentCodePoint);
            continue;
        }
        // contract with translators is that they have to understand codepoints
        // and they just took care of a surrogate pair
        for (int pt = 0; pt < consumed; pt++) {
            pos += Character.charCount(Character.codePointAt(input, pos));
        }
    }
}