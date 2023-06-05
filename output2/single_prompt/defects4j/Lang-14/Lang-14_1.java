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
    if(cs1.length() != cs2.length()) {
        return false;
    }
    return cs1.toString().equals(cs2.toString());
} 

The fix includes checking the length of the two CharSequences to ensure they are the same length before comparing their contents. This is necessary because the equals() method does not consider two CharSequences to be equal unless they have the same length and contain the same characters in the same order.