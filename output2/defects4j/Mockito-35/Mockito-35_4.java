## Fixed Function 1
public static <T> T isA(Class<T> clazz) {
    return Matchers.isA(clazz);
}

## Fixed Function 2
public static <T> T eq(T value) {
    return Matchers.eq(value);
}

## Fixed Function 3
public static <T> T same(T value) {
    return Matchers.same(value);
}