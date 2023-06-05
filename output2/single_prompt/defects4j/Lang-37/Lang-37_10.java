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
    // Fix: Cast the array2 argument to the same component type as array1
    if (array2.getClass().getComponentType().isAssignableFrom(type1)) {
        T[] tempArray = (T[]) array2;
        for (int i = array1.length; i < joinedArray.length; i++) {
            joinedArray[i] = tempArray[i - array1.length];
        }
    }
    return joinedArray;
}