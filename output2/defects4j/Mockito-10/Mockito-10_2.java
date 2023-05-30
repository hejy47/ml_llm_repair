## Fixed Function 1
private Object deepStub(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
    // matches invocation for verification
    for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
        if (stubbedInvocationMatcher.matches(invocation)) { // Fixed: replaced container.getInvocationForStubbing() with invocation
            return stubbedInvocationMatcher.answer(invocation);
        }
    }
    // record deep stub answer
    return recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata), container);
}

## Fixed Function 2
private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) {
    return mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata).build()); // Fixed: added .build() to withSettingsUsing()
}

## Fixed Function 3
private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
    MockSettings mockSettings = returnTypeGenericMetadata.hasRawExtraInterfaces() ? withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();
    return mockSettings.serializable().defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).withoutAnnotations(); // Fixed: added .withoutAnnotations() 
}