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
    KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
    if (column >= 0) {
        return rowData.getObject(columnKey);
    } else {
        return null;
    }
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex >= 0) {
        KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
        row.removeValue(columnKey);
        // 1. check whether the row is now empty.
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
    }
    // 2. check whether the column is now empty.
    int columnIndex = getColumnIndex(columnKey);
    if (columnIndex >= 0) {
        allNull = true;
        for (int i = 0, rowCount = getRowCount(); i < rowCount; i++) {
            KeyedObjects row = (KeyedObjects) this.rows.get(i);
            if (row.getObject(columnIndex) != null) {
                allNull = false;
                break;
            }
        }
        if (allNull) {
            Comparable removed = this.columnKeys.remove(columnIndex);
            for (int i = 0, rowCount = getRowCount(); i < rowCount; i++) {
                KeyedObjects row = (KeyedObjects) this.rows.get(i);
                row.removeValue(removed);
            }
        }
    }
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int index = getRowIndex(rowKey);
    if (index >= 0) {
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
    this.columnKeys.remove(index);
    for (int i = 0, rowCount = getRowCount(); i < rowCount; i++) {
        KeyedObjects row = (KeyedObjects) this.rows.get(i);
        row.removeValue(columnKey);
    }
}