## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());
    Class<?> rawType = returnTypeGenericMetadata.rawType();
    if (!new MockCreationValidator().isTypeMockable(rawType)) {
        return delegate.returnValueFor(rawType);
    }
    return mockCreationSettings.getMockName().getMockitoMock().get(returnTypeGenericMetadata.resolveType(), invocation.getArgument(0));
}

## Fixed Function 2
private Object getMock(InvocationOnMock invocation) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
    // matches invocation for verification
    for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
        if (container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())) {
            return stubbedInvocationMatcher.answer(invocation);
        }
    }
    // deep stub
    return handleDeepStubbing(invocation, container);
}

private Object handleDeepStubbing(InvocationOnMock invocation, InvocationContainerImpl container) throws Throwable {
    Class<?> returnType = invocation.getMethod().getReturnType();
    MockCreationSettings<?> mockSettings = new MockSettingsImpl().defaultAnswer(Mockito.RETURNS_DEEP_STUBS).getMockCreationSettings();

    MockitoMock<?> mock = mockSettings.getMockName().getMockitoMock();
    InvocationContainerImpl mockInvocationContainer = (InvocationContainerImpl) mock.getInvocationContainer();
    Invocation controlInvocation = controlFor(invocation.getMock()).getLastInvocation();

    MockHandler<?> mockHandler = new MockHandlerImpl<>(mock, mockInvocationContainer, mockSettings, uninitializedMockHandlerSupplier());
    MockCreationSettings<?> forwardedSettings = mockSettings.copy().name(mockSettings.getName() + "->" + controlInvocation.toString()).outerInstance(invocation.getMock()).invocationContainer(mockInvocationContainer);
    forwardedSettings.setInvoker(invocation.getMock());
    mockInvocationContainer.setInvoker(invocation.getMock());

    mockHandler.setMockSettings(forwardedSettings);
    mockInvocationContainer.setStubFor(controlInvocation, new ReturnValues(invocation, mockHandler, returnType));

    return mock.get(returnType, invocation.getArgument(0));
}

## Fixed Function 3
private Object handleDeepStubbing(InvocationOnMock invocation, InvocationContainerImpl container) throws Throwable {
    Class<?> returnType = invocation.getMethod().getReturnType();
    MockCreationSettings<?> mockSettings = new MockSettingsImpl().defaultAnswer(Mockito.RETURNS_DEEP_STUBS).getMockCreationSettings();

    MockitoMock<?> mock = mockSettings.getMockName().getMockitoMock();
    InvocationContainerImpl mockInvocationContainer = (InvocationContainerImpl) mock.getInvocationContainer();
    Invocation controlInvocation = controlFor(invocation.getMock()).getLastInvocation();

    MockHandler<?> mockHandler = new MockHandlerImpl<>(mock, mockInvocationContainer, mockSettings, uninitializedMockHandlerSupplier());
    MockCreationSettings<?> forwardedSettings = mockSettings.copy().name(mockSettings.getName() + "->" + controlInvocation.toString()).outerInstance(invocation.getMock()).invocationContainer(mockInvocationContainer);
    forwardedSettings.setInvoker(invocation.getMock());
    mockInvocationContainer.setInvoker(invocation.getMock());

    mockHandler.setMockSettings(forwardedSettings);
    mockInvocationContainer.setStubFor(controlInvocation, new ReturnValues(invocation, mockHandler, returnType));

    return mock.get(returnType, invocation.getArgument(0));
}