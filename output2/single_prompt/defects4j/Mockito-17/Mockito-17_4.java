## Fixed Function 1
public MockSettings serializable() {
    return this.extraInterfaces(Serializable.class);
}

## Fixed Function 2
public boolean isSerializable() {
    return extraInterfaces != null && Arrays.asList(extraInterfaces).contains(Serializable.class);
}

## Fixed Function 3
public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
    creationValidator.validateType(classToMock);
    creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
    creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
    settings.initiateMockName(classToMock);
    MockHandler<T> mockHandler = new MockHandler<>(settings);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
    Class<?>[] interfaces = settings.getExtraInterfaces();
    Class<?>[] ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
    Object spiedInstance = settings.getSpiedInstance();
    T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
    if (spiedInstance != null) {
        new LenientCopyTool().copyToMock(spiedInstance, mock);
    }
    return mock;
}