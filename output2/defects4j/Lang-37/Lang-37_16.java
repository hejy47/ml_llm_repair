## Fixed Function 1
@SuppressWarnings("unchecked")
public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    final Class<?> type1 = array1.getClass().getComponentType();
    // Fixed: Use Array.newInstance on the common type of both arrays
    final Class<?> type2 = array2.getClass().getComponentType();
    T[] joinedArray = (T[]) Array.newInstance(type1.isAssignableFrom(type2) ? type1 : type2, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}