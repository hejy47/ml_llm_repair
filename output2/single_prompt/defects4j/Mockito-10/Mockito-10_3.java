## Fixed Function 1
private Object deepStub(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainer container = handler.getInvocationContainer(); // use the interface instead of implementation
    // matches invocation for verification
    for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
        if (invocation.matches(stubbedInvocationMatcher.getInvocation())) { // use matches() method of the invocation
            return stubbedInvocationMatcher.answer(invocation);
        }
    }
    // record deep stub answer
    return recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata), container);
}

## Fixed Function 2
private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) {
    return mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata).defaultAnswer(CALLS_REAL_METHODS)); // specify default answer as CALLS_REAL_METHODS for deep stubs
}

## Fixed Function 3
private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
    MockSettings mockSettings = returnTypeGenericMetadata.hasRawExtraInterfaces() ? withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();
    return mockSettings.serializable().defaultAnswer(RETURNS_DEFAULTS); // specify default answer as RETURNS_DEFAULTS for top-level mocks
}