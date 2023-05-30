## Fixed Function 1
public Object answer(InvocationOnMock invocation) {
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
        //mocks by default should return 0 if references are the same, otherwise some other value because they are not the same. Hence we return a negative value (anything below 0 is good).
        //Only for compareTo() method by the Comparable interface
        return -1;
    } else {
        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }
}