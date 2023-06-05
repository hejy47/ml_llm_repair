public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Character.MAX_CODE_POINT;  // change from Integer.MAX_VALUE to Character.MAX_CODE_POINT
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
            ch = chars[random.nextInt(chars.length)];  // change to use chars.length instead of gap
        }
        if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
            if (Character.isSurrogate(ch)) {  // check for surrogate pair
                if (count == 0) {
                    count++;
                } else if (Character.isHighSurrogate(ch)) {
                    // high surrogate, insert low surrogate before putting it in
                    buffer[count] = (char) (Character.MIN_LOW_SURROGATE + random.nextInt(Character.MAX_LOW_SURROGATE - Character.MIN_LOW_SURROGATE + 1));
                    count--;
                    buffer[count] = ch;
                } else { // low surrogate, insert high surrogate after putting it in
                    buffer[count] = ch;
                    count--;
                    buffer[count] = (char) (Character.MIN_HIGH_SURROGATE + random.nextInt(Character.MAX_HIGH_SURROGATE - Character.MIN_HIGH_SURROGATE + 1));
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