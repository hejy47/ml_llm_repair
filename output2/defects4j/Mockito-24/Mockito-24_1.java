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
        //mocks by default should return 0 if references are the same, otherwise some other value because they are not the same. Hence we return 1 (anything but 0 is good).
        //Only for compareTo() method by the Comparable interface
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnType == Integer.TYPE || returnType == Integer.class) {
            return 1;
        } else if (returnType == Long.TYPE || returnType == Long.class) {
            return (long) 1;
        } else if (returnType == Double.TYPE || returnType == Double.class) {
            return (double) 1;
        } else if (returnType == Float.TYPE || returnType == Float.class) {
            return (float) 1;
        } else if (returnType == Short.TYPE || returnType == Short.class) {
            return (short) 1;
        } else if (returnType == Byte.TYPE || returnType == Byte.class) {
            return (byte) 1;
        } else if (returnType == Character.TYPE || returnType == Character.class) {
            return (char) 1;
        } else if (returnType == Boolean.TYPE || returnType == Boolean.class) {
            return true;
        } else {
            return null;
        }
    }
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}