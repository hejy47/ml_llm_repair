## Fixed Function 1
public String getLine(int lineNumber) {
    String js = "";
    try {
        js = getCode();
    } catch (IOException e) {
        return null;
    }
    int pos = 0;
    int startLine = 1;
    if (lineNumber >= lastLine) {
        pos = lastOffset;
        startLine = lastLine;
    } else {
        // If we're searching for a line before our last search,
        // reset the position to the beginning of the file.
        lastOffset = 0;
        lastLine = 1;
    }
    for (int n = startLine; n < lineNumber; n++) {
        int nextpos = js.indexOf('\n', pos);
        if (nextpos == -1) {
            return null;
        }
        pos = nextpos + 1;
    }
    lastOffset = pos;
    lastLine = lineNumber;
    // Check if we've reached the end of the file
    if (pos >= js.length()) {
        return null;
    }
    // If we haven't reached the end of the file, find the next line break.
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
        // If we can't find the next line break, return the rest of the file.
        return js.substring(pos);
    } else {
        return js.substring(pos, nextpos);
    }
}