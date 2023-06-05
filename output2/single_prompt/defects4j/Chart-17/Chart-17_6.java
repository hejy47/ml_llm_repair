## Fixed Function 1
public Object clone() throws CloneNotSupportedException {
    Chart clone = (Chart) super.clone();
    clone.data = new ArrayList<>(this.data); // create a new ArrayList with a copy of the current data
    return clone;
}