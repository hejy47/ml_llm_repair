## Fixed Function 1
public Object answer(InvocationOnMock invocation) throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());
    Class<?> rawType = returnTypeGenericMetadata.rawType();
    if (rawType != null && !mockitoCore.isTypeMockable(rawType)) {
        return delegate.returnValueFor(rawType);
    }
    return getMock(invocation, returnTypeGenericMetadata);
}

## Fixed Function 2
private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
    Class<?>[] rawExtraInterfaces = returnTypeGenericMetadata.rawExtraInterfaces();
    MockSettings mockSettings = rawExtraInterfaces.length > 0 ? withSettings().extraInterfaces(rawExtraInterfaces) : withSettings();
    return mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
}

## Fixed Function 3
private Object recordDeepStubMock(final Object mock, InvocationContainerImpl container) throws Throwable {
    container.setAnswerForVoidMock(new Answer<Object>() {

        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }, false);
    return mock;
}