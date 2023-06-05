## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());
    Class<?> rawType = returnTypeGenericMetadata.rawType();
    if (!new MockCreationValidator().isTypeMockable(rawType)) {
        return delegate.returnValueFor(rawType);
    }
    return getMock(invocation, rawType);
}

## Fixed Function 2
private Object getMock(InvocationOnMock invocation, Class<?> rawType) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
    // matches invocation for verification
    for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
        if (container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())) {
            return stubbedInvocationMatcher.answer(invocation);
        }
    }
    // deep stub
    return recordDeepStubMock(rawType, container);
}

## Fixed Function 3
private Object recordDeepStubMock(Class<?> clz, InvocationContainerImpl container) {
    final Object mock = org.mockito.Mockito.mock(clz, this);
    container.setAdditionalAnswer(new Answer<Object>() {

        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    });
    return mock;
}