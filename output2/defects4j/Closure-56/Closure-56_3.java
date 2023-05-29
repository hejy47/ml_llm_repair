## Fixed Function 1
public String getLine(int lineNumber) {
    String js = "";
    try {
        js = getCode(); //remove unnecessary comment
    } catch (IOException e) {
        return null;
    }
    int pos = 0;
    int startLine = 1;
    if (lineNumber >= lastLine) {
        pos = lastOffset;
        startLine = lastLine;
    }
    int currentLine = startLine; //create variable to keep track of current line number
    while (currentLine < lineNumber) { //use while loop to iterate until current line number matches target line number
        int nextpos = js.indexOf('\n', pos);
        if (nextpos == -1) {
            return null;
        }
        pos = nextpos + 1;
        currentLine++; //increment currentLine variable
    }
    lastOffset = pos;
    lastLine = lineNumber;
    int nextLinePos = js.indexOf('\n', pos);
    if (nextLinePos == -1) { //check if next new line can be found
        if (pos >= js.length()) { //check if pos already reaches end of file
            return null;
        }
        else {
            return js.substring(pos); //if not, return contents between pos and end of file
        }
    } else {
        return js.substring(pos, nextLinePos);
    }
}