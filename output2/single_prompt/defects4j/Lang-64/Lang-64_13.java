## Fixed Function 1
public int compareTo(Object other) {
    if (!(other instanceof ValuedEnum)) {
        throw new ClassCastException("Cannot compare ValuedEnum with non-ValuedEnum");
    }
    return iValue - ((ValuedEnum) other).iValue;
}