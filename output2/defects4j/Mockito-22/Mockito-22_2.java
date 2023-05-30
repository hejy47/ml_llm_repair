## Fixed Function 1
public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual((Object[]) o1, (Object[]) o2); // cast o1 and o2 as Object arrays
    } else {
        return o1.equals(o2);
    }
}