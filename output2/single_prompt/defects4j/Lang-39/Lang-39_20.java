private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    if (text == null || text.isEmpty() || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
        return text;
    }
    if (timeToLive < 0) {
        throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }
    int searchLength = searchList.length;
    int replacementLength = replacementList.length;
    if (searchLength != replacementLength) {
        throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
    }
    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
    int textIndex;
    int replaceIndex;
    int tempIndex;
    StringBuilder buf = new StringBuilder(text.length() + Math.min(20, text.length() / 5));
    while (true) {
        textIndex = Integer.MAX_VALUE;
        replaceIndex = -1;
        for (int i = 0; i < searchLength; i++) {
            if (!noMoreMatchesForReplIndex[i] && (searchList[i] != null && !searchList[i].isEmpty() &&
                    replacementList[i] != null)) {
                tempIndex = text.indexOf(searchList[i]);
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else if (tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        if (textIndex == Integer.MAX_VALUE) {
            break;
        }
        buf.append(text, 0, textIndex).append(replacementList[replaceIndex]);
        text = text.substring(textIndex + searchList[replaceIndex].length());
    }
    buf.append(text);
    String result = buf.toString();
    if (repeat && timeToLive > 0 && result.contains(String.join("", searchList))) {
        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }
    return result;
}