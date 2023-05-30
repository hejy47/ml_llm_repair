## Fixed Function 1
public static <T> T isA(Class<T> clazz) {
    return reportMatcher(new InstanceOf(clazz)).returnTypeUnsafe();
}

## Fixed Function 2
public static <T> T eq(T value) {
    return reportMatcher(new Equals(value)).returnTypeUnsafe();
}

## Fixed Function 3
public static <T> T same(T value) {
    return reportMatcher(new Same(value)).returnTypeUnsafe();
}