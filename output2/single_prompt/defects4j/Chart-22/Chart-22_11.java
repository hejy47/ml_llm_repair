## Fixed Function 1
public Object getObject(Comparable rowKey, Comparable columnKey) {
    if (rowKey == null) {
        throw new IllegalArgumentException("Null 'rowKey' argument.");
    }
    if (columnKey == null) {
        throw new IllegalArgumentException("Null 'columnKey' argument.");
    }
    int row = this.rowKeys.indexOf(rowKey);
    if (row < 0) {
        throw new UnknownKeyException("Row key (" + rowKey + ") not recognised.");
    }
    int column = this.columnKeys.indexOf(columnKey);
    if (column < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    }
    if (column >= 0) { // Check if column exists before fetching from row data
        KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
        return rowData.getObject(columnKey);
    } else {
        return null;
    }
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    // 1. check whether the row is now empty.
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex < 0) {
        return; // Row index not found, exit function
    }
    KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
    row.removeValue(columnKey); // Remove column before checking if row is now empty
    boolean allNull = true;
    for (int item = 0, itemCount = row.getItemCount(); item < itemCount; item++) {
        if (row.getObject(item) != null) {
            allNull = false;
            break;
        }
    }
    if (allNull) {
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    }
    // 2. check whether the column is now empty.
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int index = getRowIndex(rowKey);
    if (index >= 0) { // Check if row exists before removing
        removeRow(index);
    }
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    int index = getColumnIndex(columnKey);
    if (index < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    }
    Iterator iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        KeyedObjects rowData = (KeyedObjects) iterator.next();
        rowData.removeValue(columnKey);
    }
    this.columnKeys.remove(columnKey);
}