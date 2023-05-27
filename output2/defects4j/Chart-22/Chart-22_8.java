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
    } else if(row >= this.rows.size()){
        return null;
    }
    int column = this.columnKeys.indexOf(columnKey);
    if (column < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    } else if(column >= this.columnKeys.size()){
        return null;
    }
    KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
    Object obj = rowData.getObject(columnKey);
    return obj;
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex < 0 || rowIndex >= this.rows.size()) {
        return;
    }

    KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
    if (row.getObject(columnKey) == null) {
        return;
    }
    row.removeValue(columnKey);

    // 1. check whether the row is now empty.
    boolean allNull = true;
    for (int i = 0; i < row.getItemCount(); i++) {
        if (row.getObject(i) != null) {
            allNull = false;
            break;
        }
    }
    if (allNull) {
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    }
    // 2. check whether the column is now empty.
    boolean emptyColumn = true;
    for (int i = 0; i < this.rows.size(); i++) {
        KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
        if (rowData.getObject(columnKey) != null) {
            emptyColumn = false;
            break;
        }
    }
    if (emptyColumn) {
        this.columnKeys.remove(columnKey);
        for (int i = 0; i < this.rows.size(); i++) {
            KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
            rowData.removeValue(columnKey);
        }
    }
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int rowIndex = getRowIndex(rowKey);
    if (rowIndex >= 0 && rowIndex < this.rows.size()) {
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    }
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    int index = getColumnIndex(columnKey);
    if (index < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    } else if(index >= this.columnKeys.size()){
        return;
    }
    
    Iterator iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        KeyedObjects rowData = (KeyedObjects) iterator.next();
        if (rowData.getObject(columnKey) != null) {
            rowData.removeValue(columnKey);
        }
    }
    this.columnKeys.remove(columnKey);
}