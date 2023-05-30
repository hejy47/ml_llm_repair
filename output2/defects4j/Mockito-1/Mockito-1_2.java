## Fixed Function 1
public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object vararg = invocation.getRawArguments()[indexOfVararg];
        if (vararg == null || !vararg.getClass().isArray() || !isVarargsMatcherPresent()) {
            throw new UnsupportedOperationException();
        }
        for (int i = 0; i < Array.getLength(vararg); i++) {
            Object arg = Array.get(vararg, i);
            for (Matcher m : matchers) {
                if (m instanceof CapturesArguments) {
                    ((CapturesArguments) m).captureFrom(arg);
                }
            }
        }
    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}

// Helper method to check if there is a varargs matcher in the list of matchers
private boolean isVarargsMatcherPresent() {
    for (Matcher m : matchers) {
        if (m instanceof VarargMatcher) {
            return true;
        }
    }
    return false;
}