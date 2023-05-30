## Fixed Function 1
public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    Object[] arguments = i.getArguments();
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments && k < arguments.length) {
            ((CapturesArguments) m).captureFrom(arguments[k]);
        }
        k++;
    }
}