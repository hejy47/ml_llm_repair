## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(invocation.getMock(), invocation.getMethod()).resolveGenericReturnType(invocation.getMethod());
    Class<?> rawType = returnTypeGenericMetadata.rawType();
    if (!mockitoCore.isTypeMockable(rawType)) {
        return delegate.returnValueFor(rawType);
    }
    return getMock(invocation, returnTypeGenericMetadata);
}

## Fixed Function 2
private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
    MockSettings mockSettings = returnTypeGenericMetadata.rawExtraInterfaces().length > 0 ? withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();
    return mockSettings.defaultAnswer(returnsDeepStubsAnswer(returnTypeGenericMetadata));
}

## Fixed Function 3
private Object recordDeepStubMock(final Object mock, InvocationContainerImpl container) throws Throwable {
    container.addAnswer(new Answer<Object>() {

        public Object answer(InvocationOnMock invocation) throws Throwable {
            return invocation.getMock();
        }
    }, false);
    return mock;
}