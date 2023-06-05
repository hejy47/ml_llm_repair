## Fixed Function 1
private Object deepStub(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
    InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
    // matches invocation for verification
    for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbingsDescending()) { // change to getStubbingsDescending
        if (stubbedInvocationMatcher.getInvocation().matches(invocation)) { // change to compare reversed invocation
            return stubbedInvocationMatcher.answer(invocation);
        }
    }
    // record deep stub answer
    return recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata), container);
}


## Fixed Function 2
private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) {
    return mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata).defaultAnswer(returnsDeepStubsAnswer())); // add defaultAnswer
}


## Fixed Function 3
private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
    MockSettings mockSettings = returnTypeGenericMetadata.hasRawExtraInterfaces() ? withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();
    return mockSettings.serializable();
}