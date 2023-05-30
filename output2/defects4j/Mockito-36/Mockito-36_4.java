## Fixed Function 1
public Object callRealMethod() throws Throwable {
    if (realMethod != null) {
        return realMethod.invoke(mock, rawArguments);
    } else {
        throw new IllegalStateException("Real method not found, cannot invoke.");
    }
}