## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());
    Class<?> rawType = returnTypeGenericMetadata.rawType();
    if (!new MockCreationValidator().isTypeMockable(rawType)) {
        throw new org.mockito.exceptions.misusing.UnfinishedStubbingException();
    }
    return getMock(invocation);
}

## Fixed Function 2
private Object getMock(InvocationOnMock invocation) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainer container = handler.getInvocationContainer();
    // matches invocation for verification
    for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
        if (stubbedInvocationMatcher.matches(invocation)) {
            return stubbedInvocationMatcher.answer(invocation);
        }
    }
    // deep stub
    return recordDeepStubMock(invocation, container);
}

## Fixed Function 3
private Object recordDeepStubMock(InvocationOnMock invocation, InvocationContainer container) {
    Class<?> clz = invocation.getMethod().getReturnType();
    final Object mock = org.mockito.Mockito.mock(clz, this);
    container.setAnswersForStubbing(Arrays.asList(new Returns(mock)), false);
    return mock;
}