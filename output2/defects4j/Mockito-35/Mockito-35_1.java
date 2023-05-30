## Fixed Function 1
public static <T> T isA(Class<T> clazz) {
    return reportMatcher(new InstanceOf(clazz)).<T, T>returnTypeCheckedNull();
}

## Fixed Function 2
public static <T> T eq(T value) {
    return reportMatcher(new Equals(value)).<T, T>returnTypeCheckedNull();
}

## Fixed Function 3
public static <T> T same(T value) {
    return reportMatcher(new Same(value)).<T, T>returnTypeCheckedNull();
}