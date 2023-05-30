## Fixed Function 1
private void handleBlockComment(Comment comment) {
    if (comment.getValue().indexOf("/* @") != -1 || comment.getValue().indexOf("\n * @") != -1) {
        errorReporter.warning(SUSPICIOUS_COMMENT_WARNING, sourceName, comment.getLineno(), "", null);
    }
} 

## Fixed Function 2
private void handleBlockComment(Comment comment) {
    if (comment.getValue().indexOf("/* @") >= 0 || comment.getValue().indexOf("\r\n * @") >= 0 || comment.getValue().indexOf("\n * @") >= 0) {
        errorReporter.warning(SUSPICIOUS_COMMENT_WARNING, sourceName, comment.getLineno(), "", null);
    }
} 

## Fixed Function 3
private void handleBlockComment(Comment comment) {
    if (comment.getValue().matches(".*(\\* @|/\\*@).*")) {
        errorReporter.warning(SUSPICIOUS_COMMENT_WARNING, sourceName, comment.getLineno(), "", null);
    }
}