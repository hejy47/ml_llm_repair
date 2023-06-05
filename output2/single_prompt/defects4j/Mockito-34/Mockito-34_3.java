## Fixed Function 1
public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    Object[] args = i.getArguments();
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments && k < args.length) {
            ((CapturesArguments) m).captureFrom(args[k]);
        }
        k++;
    }
}