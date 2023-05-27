## Fixed Function 1
public void removeValue(int index) {
    if (index >= 0 && index < this.keys.size()) {
        this.keys.remove(index);
        this.values.remove(index);
        if (index < this.keys.size()) {
            rebuildIndex();
        }
    }
}

## Fixed Function 2
public void removeValue(Comparable key) {
    int index = getIndex(key);
    if (index >= 0) {
        removeValue(index);
    }
}

## Fixed Function 3
public void removeColumn(Comparable columnKey) {
    Iterator<DefaultKeyedValues> iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        DefaultKeyedValues rowData = iterator.next();
        rowData.removeValue(columnKey);
    }
    this.columnKeys.remove(columnKey);
}