## Fixed Function 1
public void toSource(final CodeBuilder cb, final int inputSeqNum, final Node root) {
    runInCompilerThread(new Callable<Void>() {

        public Void call() throws Exception {
            if (options.printInputDelimiter) {
                if ((cb.getLength() > 0) && !cb.endsWith("\n")) {
                    // Make sure that the label starts on a new line
                    cb.append("\n");
                }
                Preconditions.checkState(root.getType() == Token.SCRIPT);
                String delimiter = options.inputDelimiter;
                String sourceName = (String) root.getProp(Node.SOURCENAME_PROP);
                Preconditions.checkState(sourceName != null);
                Preconditions.checkState(!sourceName.isEmpty());
                delimiter = delimiter.replaceAll("%name%", sourceName).replaceAll("%num%", String.valueOf(inputSeqNum));
                cb.append(delimiter).append("\n");
            }
            if (root.getJSDocInfo() != null && root.getJSDocInfo().getLicense() != null) {
                cb.append("/*\n").append(root.getJSDocInfo().getLicense()).append("*/\n");
            }
            // If there is a valid source map, then indicate to it that the current
            // root node's mappings are offset by the given string builder buffer.
            if (options.sourceMapOutputPath != null) {
                sourceMap.setStartingPosition(cb.getLineIndex(), cb.getColumnIndex());
            }
            // if LanguageMode is ECMASCRIPT5_STRICT, only print 'use strict'
            // for the first input file
            String code = toSourceHelper(root, sourceMap);
            if (!code.isEmpty()) {
                cb.append(code);
                // In order to avoid parse ambiguity when files are concatenated
                // together, all files should end in a semi-colon. Do a quick
                // heuristic check if there's an obvious semi-colon already there.
                int length = code.length();
                char lastChar = code.charAt(length - 1);
                char secondLastChar = length >= 2 ? code.charAt(length - 2) : '\0';
                boolean hasSemiColon = lastChar == ';' || (lastChar == '\n' && secondLastChar == ';');
                if (!hasSemiColon) {
                    cb.append(";");
                }
            }
            return null;
        }
    });
}

## Fixed Function 2
@Override
String toSource(Node n) {
    initCompilerOptionsIfTesting();
    if (options.getOutputCharset() == null) {
        options.setOutputCharset("UTF-8");
    }
    return toSourceHelper(n, null);
}

## Fixed Function 3
private String toSourceHelper(Node n, SourceMap sourceMap) {
    CodePrinter.Builder builder = new CodePrinter.Builder(n);
    builder.setPrettyPrint(options.prettyPrint);
    builder.setLineBreak(options.lineBreak);
    builder.setSourceMap(sourceMap);
    builder.setSourceMapDetailLevel(options.sourceMapDetailLevel);
    builder.setTagAsStrict(options.getLanguageOut() == LanguageMode.ECMASCRIPT5_STRICT);
    builder.setLineLengthThreshold(options.lineLengthThreshold);
    Charset charset = options.getOutputCharset() != null ? Charset.forName(options.getOutputCharset()) : null;
    builder.setOutputCharset(charset);
    return builder.build();
}