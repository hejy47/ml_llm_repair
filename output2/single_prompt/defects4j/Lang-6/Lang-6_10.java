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
            int codepoint = Character.codePointAt(input, pos);
            if (Character.isSupplementaryCodePoint(codepoint)) {
                pos += 2;
            } else {
                pos += 1;
            }
            out.write(codepoint);
            continue;
        }
        // contract with translators is that they have to understand codepoints
        // and they just took care of a surrogate pair
        for (int pt = 0; pt < consumed; pt++) {
            int codepoint = Character.codePointAt(input, pos + pt);
            if (Character.isSupplementaryCodePoint(codepoint)) {
                pt++;
            }
        }
        out.write(input.subSequence(pos, pos + consumed).toString());
        pos += consumed;
    }
}