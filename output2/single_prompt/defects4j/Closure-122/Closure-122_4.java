## Fixed Function 1
private void handleBlockComment(Comment comment) {
    if (comment.getValue().indexOf("/* @") != -1 || comment.getValue().contains("\n * @")) {
        errorReporter.warning(SUSPICIOUS_COMMENT_WARNING, sourceName, comment.getLineno(), "", 0);
    }
}