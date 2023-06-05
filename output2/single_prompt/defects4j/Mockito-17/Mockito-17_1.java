## Fixed Function 1
public MockSettings serializable() {
    return this.extraInterfaces(java.io.Serializable.class);
}

//fix:
public MockSettings serializable() {
    return this.extraInterfaces(java.io.Serializable.class);
}


## Fixed Function 2
public boolean isSerializable() {
    return extraInterfaces != null && java.util.Arrays.asList(extraInterfaces).contains(java.io.Serializable.class);
}

//fix:
public boolean isSerializable() {
    return extraInterfaces != null && Arrays.stream(extraInterfaces).anyMatch(c -> c.equals(java.io.Serializable.class));
}


## Fixed Function 3
public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
    creationValidator.validateType(classToMock);
    creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
    creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
    settings.initiateMockName(classToMock);
    MockHandler<T> mockHandler = new MockHandler<T>(settings);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
    Class<?>[] interfaces = settings.getExtraInterfaces();
    Class<?>[] ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
    Object spiedInstance = settings.getSpiedInstance();
    if (spiedInstance != null && !classToMock.isInstance(spiedInstance)) {
        throw new MockitoException("Cannot use doReturn() with null argument. If <T> is a primitive or a final class, " +
                 "it cannot be mocked.");
    }
    T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
    if (spiedInstance != null) {
        new LenientCopyTool().copyToMock(spiedInstance, mock);
    }
    return mock;
}

//fix:
public <T> T createMock(Class<T> classToMock, MockSettings settings) {
    creationValidator.validateType(classToMock);
    creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
    creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
    settings.initiateMockName(classToMock);
    MockHandler<T> mockHandler = new MockHandler<>(settings);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
    Class<?>[] interfaces = settings.getExtraInterfaces();
    Class<?>[] ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
    Object spiedInstance = settings.getSpiedInstance();
    if (spiedInstance != null && !classToMock.isInstance(spiedInstance)) {
        throw new MockitoException("Argument must not be null and must be an instance of the mocked class");
    }
    T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
    if (spiedInstance != null) {
        new LenientCopyTool().copyToMock(spiedInstance, mock);
    }
    return mock;
}