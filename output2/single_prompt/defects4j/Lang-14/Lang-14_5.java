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
    return cs1.toString().equals(cs2.toString());
} 

The fixed function checks if the two sequences are equal after converting them to strings.