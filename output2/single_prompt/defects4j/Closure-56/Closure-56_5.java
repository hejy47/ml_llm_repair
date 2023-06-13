## Fixed Function 1
public String getLine(int lineNumber) {
    String js = "";
    try {
        // NOTE(nicksantos): Right now, this is optimized for few warnings.
        // This is probably the right trade-off, but will be slow if there
        // are lots of warnings in one file.
        js = getCode();
    } catch (IOException e) {
        return null;
    }
    int pos = 0;
    int startLine = 1;
    // If we've saved a previous offset and it's for a line less than the
    // one we're searching for, then start at that point.
    if (lineNumber >= lastLine) {
        pos = lastOffset;
        startLine = lastLine;
    }
    for (int n = startLine; n <= lineNumber; n++) { // Changed < to <= so that the correct line number is returned
        int nextpos = js.indexOf('\n', pos);
        if (nextpos == -1) {
            // If next new line cannot be found, there are two cases
            // 1. pos already reaches the end of file, then null should be returned
            // 2. otherwise, return the contents between pos and the end of file.
            if (pos == js.length()) {
                return null;
            } else {
                return js.substring(pos); // Return the rest of the file if the line number is beyond the end of the file
            }
        }
        pos = nextpos + 1;
    }
    // Remember this offset for the next search we do.
    lastOffset = pos;
    lastLine = lineNumber;
    // Return the contents of the line
    return js.substring(pos, js.indexOf('\n', pos));
}