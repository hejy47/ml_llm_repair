## Fixed Function 1
public static <T> T[] add(T[] array, T element) {
    Class<?> type;
    if (array != null) {
        type = array.getClass().getComponentType();
    } else if (element != null) {
        type = element.getClass();
    } else {
        throw new IllegalArgumentException("Both input array and element are null");
    }

    @SuppressWarnings("unchecked")
    T[] newArray = (T[]) Array.newInstance(type, array.length + 1);
    System.arraycopy(array, 0, newArray, 0, array.length);
    newArray[array.length] = element;
    return newArray;
}

## Fixed Function 2
public static <T> T[] add(T[] array, int index, T element) {
    if (index > array.length || index < 0) {
        throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + array.length);
    }

    Class<?> clss = null;
    if (array != null) {
        clss = array.getClass().getComponentType();
    } else if (element != null) {
        clss = element.getClass();
    } else {
        throw new IllegalArgumentException("Both input array and element are null");
    }

    @SuppressWarnings("unchecked")
    final T[] newArray = (T[]) Array.newInstance(clss, array.length + 1);
    System.arraycopy(array, 0, newArray, 0, index);
    newArray[index] = element;
    System.arraycopy(array, index, newArray, index + 1, array.length - index);
    return newArray;
}