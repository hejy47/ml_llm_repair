## Fixed Function 1
public <T> T mock(Class<T> classToMock, MockSettings mockSettings, boolean shouldResetOngoingStubbing) {
    if (shouldResetOngoingStubbing) {
        mockingProgress.reset();
    }
    return mock(classToMock, mockSettings);
}

## Fixed Function 2
public <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
    mockingProgress().validateState();
    mockingProgress().reset();
    return mockUtil().createMock(classToMock, (MockSettingsImpl) mockSettings);
}

## Fixed Function 3
public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
    return MOCKITO_CORE.mock(classToMock, mockSettings != null ? mockSettings : withSettings());
}

## Fixed Function 4
@SuppressWarnings("unchecked")
public static <T> T spy(T object) {
    return (T) MOCKITO_CORE.mock(new MockSettingsImpl<T>().spiedInstance(object).defaultAnswer(CALLS_REAL_METHODS));
}