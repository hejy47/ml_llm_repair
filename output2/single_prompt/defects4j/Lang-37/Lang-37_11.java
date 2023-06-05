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
    // Check for incompatible types and cast if necessary
    if (type1.isInstance(array2[0])) {
        return joinedArray;
    } else {
        // cast the joined array to the common superclass of the component types
        Class<?> commonType = type1.isAssignableFrom(array2.getClass().getComponentType()) ? type1 : array2.getClass().getComponentType();
        T[] castedArray = (T[]) Array.newInstance(commonType, joinedArray.length);
        for (int i = 0; i < joinedArray.length; i++) {
            castedArray[i] = commonType.cast(joinedArray[i]);
        }
        return castedArray;
    }
}