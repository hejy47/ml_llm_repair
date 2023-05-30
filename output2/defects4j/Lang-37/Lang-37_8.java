## Fixed Function 1
@SuppressWarnings("unchecked")
public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null && array2 == null) {
        return null;
    } else if (array1 == null) {
        return array2.clone();
    } else if (array2 == null) {
        return array1.clone();
    }
    final Class<?> type1 = array1.getClass().getComponentType();
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    // Check if problem is incompatible types
    return joinedArray;
}