## Fixed Function 1
public void removeValue(int index) {
    this.keys.remove(index);
    this.values.remove(index);
    if (index < this.keys.size()) {
        rebuildIndex(index); // Fixed: Pass the correct index to rebuildIndex method
    }
}

## Fixed Function 2
public void removeValue(Comparable key) {
    int index = getIndex(key);
    if (index < 0) {
        return;
    }
    removeValue(index);
    rebuildIndex(index); // Fixed: Rebuild the index after removing a value
}

## Fixed Function 3
public void removeColumn(Comparable columnKey) {
    Iterator iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        DefaultKeyedValues rowData = (DefaultKeyedValues) iterator.next();
        rowData.removeValue(columnKey);
    }
    this.columnKeys.remove(columnKey);
    rebuildIndex(); // Fixed: Rebuild the index after removing a column
}