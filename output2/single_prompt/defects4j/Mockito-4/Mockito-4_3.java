## Fixed Function 1
public void noMoreInteractionsWanted(Invocation undesired, List<VerificationAwareInvocation> invocations) {
    ScenarioPrinter scenarioPrinter = new ScenarioPrinter();
    String scenario = scenarioPrinter.print(invocations);
    throw new NoInteractionsWanted(join("No interactions wanted here:", new LocationImpl(), "But found this interaction on mock '" + undesired.getMock() + "':", undesired.getLocation(), "The interaction was:", undesired.getInvocation()), scenario);
}

## Fixed Function 2
public void noMoreInteractionsWantedInOrder(Invocation undesired) {
    throw new VerificationInOrderFailure(join("No interactions wanted here:", new LocationImpl(), "But found this interaction on mock '" + undesired.getMock() + "':", undesired.getLocation(), "The interaction was:", undesired.getInvocation()));
}

## Fixed Function 3
public void cannotInjectDependency(Field field, Object matchingMock, Exception details) {
    throw new MockitoException(join("Mockito couldn't inject mock dependency '" + safelyGetMockName(matchingMock) + "' on field ", "'" + field + "'", "whose type '" + field.getDeclaringClass().getCanonicalName() + "' was annotated by @InjectMocks in your test.", "Also I failed because: " + exceptionCauseMessageIfAvailable(details), ""), details);
}