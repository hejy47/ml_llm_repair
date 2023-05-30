## Fixed Function 1
public int compareTo(Object other) {
    if (!(other instanceof ValuedEnum)) {
        throw new ClassCastException("Unable to compare " + 
            this.getClass() + " with " + other.getClass());
    }
    return iValue - ((ValuedEnum) other).iValue;
}