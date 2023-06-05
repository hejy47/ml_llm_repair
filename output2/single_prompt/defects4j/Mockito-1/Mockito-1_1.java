## Fixed Function 1
public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object[] varargs = (Object[]) invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < varargs.length; i++) {
            for (int position = 0; position < matchers.size() && i < varargs.length; position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments) {
                    ((CapturesArguments) m).captureFrom(varargs[i]);
                    i++;
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