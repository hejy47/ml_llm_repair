## Fixed Function 1
// Equals
//-----------------------------------------------------------------------
public static boolean equals(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
        return true;
    }
    if (cs1 == null || cs2 == null) {
        return false;
    }
    // Check for class type compatibility before calling the equals method
    if (cs1.getClass() != cs2.getClass()) {
        return false;
    }
    return cs1.equals(cs2);
}