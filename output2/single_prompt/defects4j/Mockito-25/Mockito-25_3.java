## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = new DefaultMockitoMetadata().actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());
    Class<?> rawType = returnTypeGenericMetadata.rawType();
    if (!new MockCreationValidator(new DefaultMockitoMetadata()).isTypeMockable(rawType)) {
        return UnnecessaryStubbingException.throwFor(rawType);
    }
    return getMock(invocation);
}

## Fixed Function 2
private Object getMock(InvocationOnMock invocation) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
    // matches invocation for verification
    StubbedInvocationMatcher invokingChecker = container.getInvocationForPotentialStubbing(invocation);
    if (invokingChecker != null) {
        return invokingChecker.answer(invocation);
    }
    // deep stub
    return recordDeepStubMock(invocation, container);
}

## Fixed Function 3
private Object recordDeepStubMock(InvocationOnMock invocation, InvocationContainerImpl container) {
    Class<?> clz = invocation.getMethod().getReturnType();
    final Object mock = org.mockito.Mockito.mock(clz, withSettings().stubOnly().defaultAnswer(RETURNS_DEEP_STUBS));
    container.addAnswer(new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }, false);
    return mock;
}