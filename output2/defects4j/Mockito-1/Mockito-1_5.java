## Fixed Function 1
public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object vararg = invocation.getRawArguments()[indexOfVararg];
        if (vararg != null && vararg.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(vararg); i++) {
                for (int position = 0; position < matchers.size(); position++) {
                    Matcher m = matchers.get(position);
                    if (m instanceof CapturesArguments && isVarargMatcher(m)) {
                        ((CapturesArguments) m).captureFrom(Array.get(vararg, i));
                    }
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