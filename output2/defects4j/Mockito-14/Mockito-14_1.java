## Fixed Function 1
public Object handle(Invocation invocation) throws Throwable {
    if (invocationContainerImpl.hasAnswersForStubbing()) {
        // stubbing voids with stubVoid() or doAnswer() style
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        return null;
    }
    VerificationMode verificationMode = mockingProgress.pullVerificationMode();
    InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
    mockingProgress.validateState();
    //if verificationMode is not null then someone is doing verify()
    if (verificationMode != null) {
        //We need to check if verification was started on the correct mock
        // - see VerifyingWithAnExtraCallToADifferentMockTest
        VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl.getInvocations(), invocationMatcher);
        Mockito.verify(mockingProgress.mockingDetails(invocation.getMock()).getMock(), verificationMode).verify(invocationMatcher);
        return null;
    }
    invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);
    OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(invocationContainerImpl);
    mockingProgress.reportOngoingStubbing(ongoingStubbing);
    StubbedInvocationMatcher stubbedInvocation = invocationContainerImpl.findAnswerFor(invocation);
    if (stubbedInvocation != null) {
        stubbedInvocation.captureArgumentsFrom(invocation);
        return stubbedInvocation.answer(invocation);
    } else {
        Object ret = mockSettings.getDefaultAnswer().answer(invocation);
        // redo setting invocation for potential stubbing in case of partial
        // mocks / spies.
        // Without it, the real method inside 'when' might have delegated
        // to other self method and overwrite the intended stubbed method
        // with a different one.
        invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }
}

## Fixed Function 2
public <T> T verify(T mock, VerificationMode mode) {
    if (mock == null) {
        reporter.nullPassedToVerify();
    } else if (!mockUtil.isMock(mock)) {
        reporter.notAMockPassedToVerify();
    } else {
        Mockito.verify(mock, mode);
    }
    mockingProgress.verificationStarted(mode);
    return mock;
}