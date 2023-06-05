## Fixed Function 1
public static <T> T isA(Class<T> clazz) {
    return reportMatcher(new InstanceOf(clazz)).returnNull();
}

## Fixed Function 2
public static <T> T eq(T value) {
    return reportMatcher(ArgumentMatchers.eq(value)).returnNull();
}

## Fixed Function 3
public static <T> T same(T value) {
    return reportMatcher(ArgumentMatchers.same(value)).returnNull();
}