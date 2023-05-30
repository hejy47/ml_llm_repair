## Fixed Function 1
public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object vararg = invocation.getArgumentAt(indexOfVararg, Object.class);
        if (vararg != null && vararg.getClass().isArray()) {
            int varargLength = Array.getLength(vararg);
            for (int i = 0; i < varargLength; i++) {
                Object varargElement = Array.get(vararg, i);
                for (Matcher<?> matcher : matchers) {
                    if (matcher instanceof CapturesArguments) {
                        ((CapturesArguments) matcher).captureFrom(varargElement);
                    }
                }
            }
        }
    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher<?> matcher = matchers.get(position);
            if (matcher instanceof CapturesArguments) {
                ((CapturesArguments) matcher).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}