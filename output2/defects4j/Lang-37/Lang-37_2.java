## Fixed Function 1
@SuppressWarnings("unchecked")
public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    final Class<?> type1 = array1.getClass().getComponentType();
    T[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}