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
    if (column >= 0) {
        KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
        return rowData.getObject(column);
    } else {
        return null;
    }
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
    int rowIndex = getRowIndex(rowKey);
    if(rowIndex >=0){
        KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
        int colIndex = getColumnIndex(columnKey);
        if(colIndex>=0){
            row.removeValue(columnKey);
            if(row.getItemCount() == 0){
                removeRow(rowIndex);
            }
        }
    }   
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
    int rowIndex = getRowIndex(rowKey);
    if(rowIndex>=0){
        this.rows.remove(rowIndex);
        this.rowKeys.remove(rowIndex);
    }
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
    int columnIndex = getColumnIndex(columnKey);
    if (columnIndex < 0) {
        throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
    }
    Iterator iterator = this.rows.iterator();
    while (iterator.hasNext()) {
        KeyedObjects rowData = (KeyedObjects) iterator.next();
        rowData.removeValue(columnKey);
    }
    this.columnKeys.remove(columnIndex);
}