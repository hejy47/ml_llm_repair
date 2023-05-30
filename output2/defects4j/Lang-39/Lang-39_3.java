private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
    if (text == null || text.length() == 0 || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
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
    int textIndex = -1;
    int replaceIndex = -1;
    //changing the tempIndex variable name to currentIndex for better understanding of the index
    int currentIndex = -1;
    for (int i = 0; i < searchLength; i++) {
        if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0 || replacementList[i] == null) {
            continue;
        }
        //setting the currentIndex with the return of the index of the searchList[i] substring in the given text string, starting from the currentIndex+1
        currentIndex = text.indexOf(searchList[i], currentIndex + 1);
        if (currentIndex == -1) {
            noMoreMatchesForReplIndex[i] = true;
        } else {
            if (textIndex == -1 || currentIndex < textIndex) {
                textIndex = currentIndex;
                replaceIndex = i;
            }
        }
    }
    if (textIndex == -1) {
        return text;
    }
    int start = 0;
    int increase = 0;
    for (int i = 0; i < searchList.length; i++) {
        int greater = replacementList[i].length() - searchList[i].length();
        if (greater > 0) {
            increase += 3 * greater;
        }
    }
    increase = Math.min(increase, text.length() / 5);
    StringBuilder buf = new StringBuilder(text.length() + increase);
    while (textIndex != -1) {
        for (int i = start; i < textIndex; i++) {
            buf.append(text.charAt(i));
        }
        buf.append(replacementList[replaceIndex]);
        start = textIndex + searchList[replaceIndex].length();
        textIndex = -1;
        replaceIndex = -1;
        currentIndex = -1;
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0 || replacementList[i] == null) {
                continue;
            }
            currentIndex = text.indexOf(searchList[i], start);
            if (currentIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || currentIndex < textIndex) {
                    textIndex = currentIndex;
                    replaceIndex = i;
                }
            }
        }
    }
    int textLength = text.length();
    for (int i = start; i < textLength; i++) {
        buf.append(text.charAt(i));
    }
    String result = buf.toString();
    if (!repeat) {
        return result;
    }
    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
}