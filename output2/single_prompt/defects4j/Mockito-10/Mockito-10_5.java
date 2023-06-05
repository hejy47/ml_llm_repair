## Fixed Function 1
private Object deepStub(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
    // matches invocation for verification
    StubbedInvocationMatcher invocationMatcher = container.findAnswerFor(invocation);
    if (invocationMatcher != null) {
        return invocationMatcher.answer(invocation);
    }
    // record deep stub answer
    return recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata), container);
}

## Fixed Function 2
private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) {
    return mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
}

## Fixed Function 3
private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
    MockSettings mockSettings = returnTypeGenericMetadata.hasRawExtraInterfaces() ? withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();
    return mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).serializable();
}