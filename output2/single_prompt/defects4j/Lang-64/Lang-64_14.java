## Fixed Function 1
public int compareTo(Object other) {
    if (other instanceof ValuedEnum) {
        return iValue - ((ValuedEnum) other).iValue;
    }
    throw new ClassCastException("Unable to compare objects of different classes.");
}