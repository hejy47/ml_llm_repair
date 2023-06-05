## Fixed Function 1
public static <T> T[] add(T[] array, T element) {
    Class<?> type;
    if (array != null && array.length > 0) {
        type = array.getClass().getComponentType();
    } else if (element != null) {
        type = element.getClass();
    } else {
        type = Object.class;
    }
    @SuppressWarnings("unchecked")
    T[] newArray = (T[]) Array.newInstance(type, array != null ? array.length + 1 : 1);
    if (array != null) {
        System.arraycopy(array, 0, newArray, 0, array.length);
    }
    newArray[newArray.length - 1] = element;
    return newArray;
}

## Fixed Function 2
public static <T> T[] add(T[] array, int index, T element) {
    Class<?> clss = null;
    if (array != null && array.length > 0) {
        clss = array.getClass().getComponentType();
    } else if (element != null) {
        clss = element.getClass();
    } else {
        return (T[]) new Object[] { null };
    }
    @SuppressWarnings("unchecked")
    final T[] newArray = (T[]) Array.newInstance(clss, array != null ? array.length + 1 : 1);
    if (index > array.length) {
        index = array.length;
    } else if (index < 0) {
        index = 0;
    }
    System.arraycopy(array, 0, newArray, 0, index);
    newArray[index] = element;
    System.arraycopy(array, index, newArray, index + 1, array.length - index);
    return newArray;
}