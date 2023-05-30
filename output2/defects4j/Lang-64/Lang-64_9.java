## Fixed Function 1
public int compareTo(Object other) {
    if (other == null || !(other instanceof ValuedEnum)) {
        return 1;
    }
    return iValue - ((ValuedEnum) other).iValue;
}