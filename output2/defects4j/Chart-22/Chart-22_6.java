## Fixed Function 1
public Object getObject(Comparable rowKey, Comparable columnKey) {
    if (rowKey == null) {
        throw new IllegalArgumentException("Null 'rowKey' argument.");
    }
    if (columnKey == null) {
        throw new IllegalArgumentException("Null 'columnKey' argument.");
    }
    int row = this.rowKeys.indexOf(rowKey);
    int column = this.columnKeys.indexOf(columnKey);
    if (row >= 0 && column >= 0) { // only return if both row and column are valid
        KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
        return rowData.getObject(columnKey);
    } else {
        return null;
    }
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex < 0) {
        return; // do nothing if rowKey is not found
    }
    KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
    row.removeValue(columnKey); // remove the value
    if (row.getItemCount() == 0) { // check whether the row is now empty.
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    }
    int column = getColumnIndex(columnKey);
    if (column < 0) {
        return; // do nothing if columnKey is not found
    }
    boolean columnEmpty = true;
    for (int i = 0; i < this.rows.size(); i++) { // iterate through all rows to check if the column is now empty
        KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
        if (rowData.getObject(columnKey) != null) {
            columnEmpty = false;
            break;
        }
    }
    if (columnEmpty) { // remove the column if it is empty
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
    if (index >= 0) { // only remove the row if it exists
        this.rowKeys.remove(index);
        this.rows.remove(index);
    }
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    int index = getColumnIndex(columnKey);
    if (index < 0) {
        return; // do nothing if columnKey is not found
    }
    this.columnKeys.remove(index); // remove the column key
    for (int i = 0; i < this.rows.size(); i++) { // remove the column values from all rows
        KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
        rowData.removeValue(columnKey);
    }
}