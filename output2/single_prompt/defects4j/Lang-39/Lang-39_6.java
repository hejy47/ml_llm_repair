private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    if (text == null || text.length() == 0 || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
        return text;
    }
    // if recursing, this shouldn't be less than 0
    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }
    int searchLength = searchList.length;
    int replacementLength = replacementList.length;
    if (searchLength != replacementLength) {
        throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
    }

    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
    int textIndex = -1;
    int replaceIndex = -1;
    int tempIndex = -1;
    int start = 0;
    StringBuilder buf = new StringBuilder(text.length());

    while (textIndex != -1) {
        for (int i = start; i < text.length(); i++) {
            buf.append(text.charAt(i));
        }

        textIndex = -1;
        replaceIndex = -1;
        tempIndex = -1;

        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0 || replacementList[i] == null) {
                continue;
            }

            tempIndex = text.indexOf(searchList[i], start);

            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }

        if (textIndex != -1) {
            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }

            buf.append(replacementList[replaceIndex]);
            start = textIndex + searchList[replaceIndex].length();
        }
    }

    for (int i = start; i < text.length(); i++) {
        buf.append(text.charAt(i));
    }

    String result = buf.toString();

    if (repeat && timeToLive > 0) {
        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    return result;
}