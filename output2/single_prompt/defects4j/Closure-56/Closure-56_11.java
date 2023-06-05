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
    for (int n = startLine; n < lineNumber; n++) {
        int nextpos = js.indexOf('\n', pos);
        if (nextpos == -1) {
            // If we reach the end of the file before finding the desired line,
            // then return null.
            return null;
        }
        pos = nextpos + 1;
    }
    // Remember this offset for the next search we do.
    lastOffset = pos;
    lastLine = lineNumber;
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
        // If next new line cannot be found and we've already reached the end of the file,
        // then return null.
        if (pos == js.length()) {
            return null;
        }
        // Otherwise, return everything from the current position to the end of the file.
        return js.substring(pos);
    } else {
        // Return the line between the current position and the next new line.
        return js.substring(pos, nextpos);
    }
}