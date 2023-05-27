## Fixed Function 1
public void removeValue(int index) {
    this.keys.remove(index);
    this.values.remove(index);
    if (index < this.keys.size()) {
        // Fixed: Call rebuildIndex with index parameter to ensure correct rebuilding
        rebuildIndex(index);
    }
}

## Fixed Function 2
public void removeValue(Comparable key) {
    int index = getIndex(key);
    if (index < 0) {
        return;
    }
    // Fixed: Call removeValue with index parameter to ensure correct removal
    removeValue(index);
}

## Fixed Function 3
public void removeColumn(Comparable columnKey) {
    Iterator iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        DefaultKeyedValues rowData = (DefaultKeyedValues) iterator.next();
        // Fixed: Check if columnKey exists in rowData before removing the value
        if(rowData.getIndex(columnKey) >= 0){
            rowData.removeValue(columnKey);
        }
    }
    // Fixed: Remove columnKey only if it exists in the columnKeys
    if (this.columnKeys.contains(columnKey)) {
        this.columnKeys.remove(columnKey);
    }
}