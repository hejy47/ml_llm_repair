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
    } else if (row >= this.rows.size()) {
        return null;
    }
    int column = this.columnKeys.indexOf(columnKey);
    if (column < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    } else if (column >= this.columnKeys.size()) {
        return null;
    }
    KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
    return rowData.getObject(columnKey);
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex < 0 || rowIndex >= this.rows.size()) {
        return;
    }
    KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
    row.removeValue(columnKey);
    // 1. check whether the row is now empty.
    if (row.isEmpty()) {
        this.rowKeys.remove(rowKey);
        this.rows.remove(rowIndex);
    }
    // 2. check whether the column is now empty.
    if (getColumnIndex(columnKey) < 0) {
        this.columnKeys.remove(columnKey);
        for (int i = 0; i < this.rows.size(); i++) {
            KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
            rowData.removeValue(columnKey);
        }
    }
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int index = getRowIndex(rowKey);
    if (index < 0 || index >= this.rows.size()) {
        return;
    }
    this.rowKeys.remove(rowKey);
    this.rows.remove(index);
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    int index = getColumnIndex(columnKey);
    if (index < 0 || index >= this.columnKeys.size()) {
        return;
    }
    this.columnKeys.remove(index);
    for (int i = 0; i < this.rows.size(); i++) {
        KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
        rowData.removeValue(columnKey);
    }
}