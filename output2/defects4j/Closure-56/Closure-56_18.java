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
            return null;
        }
        pos = nextpos + 1;
    }
    // Remember this offset for the next search we do.
    lastOffset = pos;
    lastLine = lineNumber;
    int nextpos = js.indexOf('\n', pos);
    if (nextpos < pos) {
        // If the next new line cannot be found, we already reached the end of file.
        return null;
    } else if (nextpos == -1) {
        // If the next new line cannot be found, return the contents between pos and the end of file.
        return js.substring(pos);
    } else {
        return js.substring(pos, nextpos);
    }
}