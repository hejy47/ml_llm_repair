## Fixed Function 1
public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == o2; // Only return true if both are null
    } else if (o1.getClass().isArray()) { // Use getClass() to check if object is an array
        return o2.getClass().isArray() && areArraysEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}