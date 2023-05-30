## Fixed Function 1
@Override
public int translate(CharSequence input, int index, Writer out) throws IOException {
    if (index + 1 < input.length() && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;
        char firstChar = input.charAt(start);
        if (start < input.length() && (firstChar == 'x' || firstChar == 'X')) {
            start++;
            isHex = true;
        }
        int end = start;
        while (end < input.length() && input.charAt(end) != ';') {
            end++;
        }
        if (end >= input.length()) {
            return 0;
        }
        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }
        out.write(entityValue);
        return end - index + 1;
    }
    return 0;
}