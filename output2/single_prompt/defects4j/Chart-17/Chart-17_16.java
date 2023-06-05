## Fixed Function 1
@Override
public Object clone() throws CloneNotSupportedException {
    Chart clone = (Chart) super.clone();
    clone.items = Arrays.copyOf(items, items.length); // shallow copy of items array
    for (int i = 0; i < size; i++) {
        clone.items[i] = (ChartItem) items[i].clone(); // deep copy of each ChartItem
    }
    return clone;
}