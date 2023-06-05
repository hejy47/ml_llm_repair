## Fixed Function 1
public Object clone() throws CloneNotSupportedException {
    Chart clone = (Chart) super.clone();
    clone.data = (Data[]) data.clone();
    for (int i = 0; i < getItemCount(); i++) {
        clone.data[i] = (Data) data[i].clone();
        clone.data[i].setParentChart(clone);
    }
    return clone;
}