## Fixed Function 1
private String formatMethodCall() {
    return invocation.getMethod().getName() + "(" + Arrays.toString(invocation.getArguments()) + ")";
}