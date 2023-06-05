## Fixed Function 1
public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else if (o1.getClass() != o2.getClass()) {
        return false;
    } else {
        return o1.equals(o2);
    }
} 

# The first buggy function is a binary search function that uses a wrong formula for calculating mid index. The correct formula should be mid = l + (r - l) / 2. 

# The second buggy function is a method for checking if two objects are equal. The issue with this function is that it doesn't handle cases where the two objects are of different types. The fix for this issue is to add an extra condition to compare the classes of the objects before calling the equals method.