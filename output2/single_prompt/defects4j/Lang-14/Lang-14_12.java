## Fixed Function 1
// Equals
//-----------------------------------------------------------------------
public static boolean equals(CharSequence cs1, CharSequence cs2) {
    if (Objects.equals(cs1, cs2)) {
        return true;
    }
    if (cs1 == null || cs2 == null) {
        return false;
    }
    return cs1.equals(cs2);
}

//Explanation:
# The fixed function adds a check using the Objects.equals() method to handle cases where both arguments are null or when they are not null and refer to different objects. This is a safer way of checking for equality when dealing with potentially null arguments.