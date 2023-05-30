## Fixed Function 1
public static <T> T[] add(T[] array, T element) {
    Class<?> type;
    if (array != null) {
        type = array.getClass().getComponentType(); // changed to getComponentType()
    } else if (element != null) {
        type = element.getClass();
    } else {
        type = Object.class;
    }
    // type must be T
    @SuppressWarnings("unchecked")
    T[] newArray = (T[]) copyArrayGrow1(array, type);
    newArray[newArray.length - 1] = element;
    return newArray;
}

## Fixed Function 2
public static <T> T[] add(T[] array, int index, T element) {
    Class<?> clss = null;
    if (array != null) {
        clss = array.getClass().getComponentType();
    } else if (element != null) {
        clss = element.getClass();
    } else {
        return (T[]) new Object[] { null };
    }
    // the add method creates an array of type clss, which is type T
    @SuppressWarnings("unchecked")
    final T[] newArray = (T[]) add(array, index, element, clss);
    return newArray;
}