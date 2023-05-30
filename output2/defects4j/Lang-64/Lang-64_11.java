## Fixed Function 1
public int compareTo(Object other) {
    if (other instanceof ValuedEnum) {
        return iValue - ((ValuedEnum) other).iValue;
    }
    throw new ClassCastException("Cannot compare ValuedEnum with non-ValuedEnum");
}