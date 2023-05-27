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
    } else {
        KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
        int column = this.columnKeys.indexOf(columnKey);
        if (column < 0) {
            throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
        } else {
            return rowData.getObject(columnKey);
        }
    }
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    if (rowKey == null) {
        throw new IllegalArgumentException("Null 'rowKey' argument.");
    }
    if (columnKey == null) {
        throw new IllegalArgumentException("Null 'columnKey' argument.");
    }
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex >= 0) {
        KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
        row.removeValue(columnKey);
    }
    // 1. check whether the row is now empty.
    boolean allNull = true;
    for (int row = 0, rowCount = this.rows.size(); row < rowCount; row++) {
        KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
        if (rowData.getObject(columnKey) != null) {
            allNull = false;
            break;
        }
    }
    if (allNull) {
        for (int row = 0; row < this.rows.size(); row++) {
            KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
            rowData.removeValue(columnKey);
        }
        this.columnKeys.remove(columnKey);
    }
    // 2. check whether the column is now empty.
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    if (rowKey == null) {
        throw new IllegalArgumentException("Null 'rowKey' argument.");
    }
    int index = getRowIndex(rowKey);
    if (index >= 0) {
        this.rowKeys.remove(index);
        this.rows.remove(index);
    }
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    if (columnKey == null) {
        throw new IllegalArgumentException("Null 'columnKey' argument.");
    }
    int index = getColumnIndex(columnKey);
    if (index >= 0) {
        Iterator iterator = this.rows.iterator();
        while (iterator.hasNext()) {
            KeyedObjects rowData = (KeyedObjects) iterator.next();
            rowData.removeValue(columnKey);
        }
        this.columnKeys.remove(index);
    }
}