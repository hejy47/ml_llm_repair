## Fixed Function 1
@Override
public Object clone() throws CloneNotSupportedException {
    Chart clone = (Chart) super.clone();
    clone.data = new ArrayList<>(this.data);
    clone.axes = new ArrayList<>(this.axes);
    clone.title = new String(this.title);
    clone.caption = new String(this.caption);
    return clone;
}