public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }
    if (start == end && start != 0 || start > end) {
        throw new IllegalArgumentException("Invalid range");
    }
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Character.MAX_VALUE;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        }
    }
    char[] buffer = new char[count];
    int gap = end - start;
    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
            if (ch >= 0xD800 && ch <= 0xDBFF) {
                if (count == 0) {
                    count++;
                } else {
                    // low surrogate, insert high surrogate after putting it in
                    buffer[count] = ch;
                    count--;
                    buffer[count] = (char) (0xDC00 + random.nextInt(0x400));
                }
            } else if (ch >= 0xDC00 && ch <= 0xDFFF) {
                if (count == 0) {
                    count++;
                } else {
                    // high surrogate, insert low surrogate before putting it in
                    buffer[count] = (char) (0xD800 + random.nextInt(0x400));
                    count--;
                    buffer[count] = ch;
                }
            } else {
                buffer[count] = ch;
            }
        } else {
            count++;
        }
    }
    return new String(buffer);
}