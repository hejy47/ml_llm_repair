## Fixed Function 1
public static <T> T isA(Class<T> clazz) {
    return Mockito.<T>argThat(org.mockito.ArgumentMatchers.isA(clazz));
}

## Fixed Function 2
public static <T> T eq(T value) {
    return Mockito.eq(value);
}

## Fixed Function 3
public static <T> T same(T value) {
    return Mockito.same(value);
}