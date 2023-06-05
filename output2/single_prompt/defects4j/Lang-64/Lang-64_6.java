## Fixed Function 1
public int compareTo(Object other) {
    if (other instanceof ValuedEnum) {
        return iValue - ((ValuedEnum) other).iValue;
    } else {
        throw new ClassCastException("Cannot compare with non-ValuedEnum object");
    }
}