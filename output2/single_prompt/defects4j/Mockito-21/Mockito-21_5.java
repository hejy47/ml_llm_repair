## Fixed Function 1
public <T> T newInstance(Class<T> cls) {
    try {
        if (outerClassInstance == null) {
            return cls.newInstance();
        } else {
            Constructor<T> c = cls.getDeclaredConstructor(outerClassInstance.getClass());
            return c.newInstance(outerClassInstance);
        }
    } catch (Exception e) {
        throw new InstantiationException("Unable to create mock instance of '" + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e);
    }
}

## Fixed Function 2
private <T> T withOuterClass(Class<T> cls) {
    try {
        Constructor<T> c = cls.getDeclaredConstructor(outerClassInstance.getClass());
        return c.newInstance(outerClassInstance);
    } catch (Exception e) {
        throw new InstantiationException("Unable to create mock instance of '" + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e);
    }
}

## Fixed Function 3
private static <T> InstantiationException paramsException(Class<T> cls, Exception e) {
    return new InstantiationException("Unable to create mock instance of '" + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e);
}