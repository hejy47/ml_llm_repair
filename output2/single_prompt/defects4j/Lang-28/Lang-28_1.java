@Override
public int translate(CharSequence input, int index, Writer out) throws IOException {
    // protect from ArrayIndexOutOfBounds
    if (input.length() <= index+1) {
        return 0;
    }
    if (input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;
        // Check input length before accessing characters
        if (input.length() > start && (input.charAt(start) == 'x' || input.charAt(start) == 'X')) {
            start++;
            isHex = true;
        }
        int end = start;
        // Check input length before accessing characters
        while (input.length() > end && input.charAt(end) != ';') {
            end++;
        }
        // Check if entire entity value is within input bounds
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
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}