## Fixed Function 1
public <T> T mock(Class<T> classToMock, MockSettings mockSettings, boolean shouldResetOngoingStubbing) {
    if (shouldResetOngoingStubbing) {
        mockingProgress.resetOngoingStubbing();
    }
    return mock(classToMock, mockSettings);
}

## Fixed Function 2
public <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
    mockingProgress.validateState();
    return mockUtil.createMock(classToMock, (MockSettingsImpl) mockSettings);
}

## Fixed Function 3
public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
    return MOCKITO_CORE.mock(classToMock, mockSettings != null ? mockSettings : withSettings());
}

## Fixed Function 4
public static <T> T spy(T object) {
    return MOCKITO_CORE.mock((Class<T>) object.getClass(), withSettings().spiedInstance(object).defaultAnswer(RETURNS_DEFAULTS));
}