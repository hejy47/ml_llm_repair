## Fixed Function 1
public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        if (matchers.size() == 1 && matchers.get(0) instanceof CapturesArguments) {
            Object vararg = invocation.getArguments()[indexOfVararg];
            if (vararg != null && vararg.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(vararg); i++) {
                    ((CapturesArguments) matchers.get(0)).captureFrom(Array.get(vararg, i));
                }
            } else {
                ((CapturesArguments) matchers.get(0)).captureFrom(vararg);
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