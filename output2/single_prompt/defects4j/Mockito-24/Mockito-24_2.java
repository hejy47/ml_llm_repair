## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    if (methodsGuru.isToString(invocation.getMethod())) {
        Object mock = invocation.getMock();
        MockName name = mockUtil.getMockName(mock);
        if (name.isDefault()) {
            return "Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
        } else {
            return name.toString();
        }
    } else if (methodsGuru.isCompareToMethod(invocation.getMethod())) {
        //see issue 184.
        //mocks by default should return 0 if references are the same, otherwise some other value because they are not the same. Hence we return 1 (anything but 0 is good).
        //Only for compareTo() method by the Comparable interface
        return (Comparable<Object>) (o1, o2) -> {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            } else {
                return o1.hashCode() - o2.hashCode();
            }
        }.compare(invocation.getArgument(0), invocation.getMock());
    }
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}