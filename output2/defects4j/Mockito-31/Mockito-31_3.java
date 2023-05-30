## Fixed Function 1
private String formatMethodCall() {
    return invocation.getMethod().getName() + formatArguments();
}

private String formatArguments() {
    Object[] args = invocation.getArguments();
    if (args == null || args.length == 0) {
        return "()";
    }
    StringBuilder builder = new StringBuilder();
    builder.append("(");
    for (int i = 0; i < args.length; i++) {
        if (i > 0) {
            builder.append(", ");
        }
        builder.append(String.valueOf(args[i]));
    }
    builder.append(")");
    return builder.toString();
}