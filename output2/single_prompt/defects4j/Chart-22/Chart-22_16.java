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
        // handle case where row exists in row keys but not in rows list
        return null;
    }
    int column = this.columnKeys.indexOf(columnKey);
    if (column < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    }
    KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
    return rowData.getObject(columnKey);
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex < 0 || rowIndex >= this.rows.size()) {
        // handle case where row does not exist
        return;
    }
    KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
    row.removeValue(columnKey);
    if (row.getItemCount() == 0) {
        // remove empty row
        this.rows.remove(rowIndex);
        this.rowKeys.remove(rowIndex);
    }
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int index = getRowIndex(rowKey);
    if (index < 0 || index >= this.rows.size()) {
        // handle case where row does not exist
        return;
    }
    this.rows.remove(index);
    this.rowKeys.remove(index);
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    int index = getColumnIndex(columnKey);
    if (index < 0 || index >= this.columnKeys.size()) {
        // handle case where column does not exist
        return;
    }
    this.columnKeys.remove(index);
    Iterator iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        KeyedObjects rowData = (KeyedObjects) iterator.next();
        rowData.removeValue(columnKey);
    }
}