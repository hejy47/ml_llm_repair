## Fixed Function 1
public MockSettings serializable() {
    return this.extraInterfaces(java.io.Serializable.class);
}

//Fixed Function 1.1
public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
    MockSettingsImpl copy = copy();
    if (this.extraInterfaces == null) {
        copy.extraInterfaces = extraInterfaces.clone();
    } else {
        copy.extraInterfaces = Arrays.copyOf(this.extraInterfaces, this.extraInterfaces.length + extraInterfaces.length);
        System.arraycopy(extraInterfaces, 0, copy.extraInterfaces, this.extraInterfaces.length, extraInterfaces.length);
    }
    return copy;
}

## Fixed Function 2
public boolean isSerializable() {
    return extraInterfaces != null && java.util.Arrays.asList(extraInterfaces).contains(java.io.Serializable.class);
}

## Fixed Function 3
public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
    creationValidator.validateType(classToMock);
    creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
    creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
    settings.initiateMockName(classToMock);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockMaker.createMockHandler(settings), settings);
    Class<?>[] interfaces = settings.getExtraInterfaces();
    Class<?>[] ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
    T mock = mockMaker.createMock(classToMock, ancillaryTypes, filter, settings.isUseConstructor(), settings.getSpiedInstance(), true, null, false);
    if (settings.isSerializable()) {
        mock = SerializationUtil.createSerializableVersion(mock, settings);
    }
    return mock;
}