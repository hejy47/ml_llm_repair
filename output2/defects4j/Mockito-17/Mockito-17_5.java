## Fixed Function 1
public MockSettings serializable() {
    return this.extraInterfaces(java.io.Serializable.class);
}

Change to:

public MockSettings serializable() {
    return this.extraInterfaces(java.io.Serializable.class);
}

## Fixed Function 2
public boolean isSerializable() {
    return extraInterfaces != null && java.util.Arrays.asList(extraInterfaces).contains(java.io.Serializable.class);
}

Change to:

public boolean isSerializable() {
    return extraInterfaces != null && java.util.Arrays.asList(extraInterfaces).contains(java.io.Serializable.class);
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
    Class<?>[] ancillaryTypes;
    ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
    Object spiedInstance = settings.getSpiedInstance();
    T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
    if (spiedInstance != null) {
        new LenientCopyTool().copyToMock(spiedInstance, mock);
    }
    return mock;
}

Change to:

public <T> T createMock(Class<T> classToMock, MockSettings settings) {
    creationValidator.validateType(classToMock);
    creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces().toArray(new Class<?>[0]));
    creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
    settings.initiateMockName(classToMock.getSimpleName());
    MockHandler<T> mockHandler = new MockHandler<T>(settings);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
    Class<?>[] interfaces = settings.getExtraInterfaces().toArray(new Class<?>[0]);
    Class<?>[] ancillaryTypes;
    ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
    Object spiedInstance = settings.getSpiedInstance();
    T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
    if (spiedInstance != null) {
        new LenientCopyTool().copyToMock(spiedInstance, mock, CopyStrategy.NOT_NULLS);
    }
    return mock;
}