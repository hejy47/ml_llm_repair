## Fixed Function 1
@SuppressWarnings("unchecked")
public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    final Class<?> type1 = array1.getClass().getComponentType();
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    // Cast array2 elements if necessary
    for (int i = array1.length; i < joinedArray.length; i++) {
        if (!type1.isInstance(array2[i - array1.length])) {
            joinedArray[i] = (T) type1.cast(array2[i - array1.length]);
        } else {
            joinedArray[i] = array2[i - array1.length];
        }
    }
    return joinedArray;
}