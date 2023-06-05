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
    if (row >= 0 && column >= 0 && row < this.rows.size()) { // added additional check for column index
        KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
        return rowData.getObject(columnKey);
    } else {
        return null;
    }
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    setObject(null, rowKey, columnKey);
    // 1. check whether the row is now empty.
    boolean allNull = true;
    int rowIndex = getRowIndex(rowKey);
    KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
    for (int item = 0, itemCount = row.getItemCount(); item < itemCount; item++) {
        if (row.getObject(item) != null) {
            allNull = false;
            break;
        }
    }
    if (allNull) {
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    } else { // added else condition to check whether column is empty
        boolean allNullColumn = true;
        for (int i = 0; i < this.rowKeys.size(); i++) {
            KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
            if (rowData.getObject(columnKey) != null) {
                allNullColumn = false;
                break;
            }
        }
        if (allNullColumn) {
            removeColumn(columnKey);
        }
    }
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int index = getRowIndex(rowKey);
    if (index >= 0 && index < this.rows.size()) { // added additional check for row index
        this.rowKeys.remove(index);
        this.rows.remove(index);
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
    this.columnKeys.remove(index); // removed columnKey and passed index instead
}