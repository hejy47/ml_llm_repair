## Fixed Function 1
public Object clone() throws CloneNotSupportedException {
    Chart clone = (Chart) super.clone();
    clone.data = (ArrayList<Data>) this.data.clone();
    return clone;
}