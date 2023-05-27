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
   if (row < 0 || column < 0) {
      return null;
   }
   KeyedObjects rowData = (KeyedObjects) this.rows.get(row);
   return rowData.getObject(columnKey);
}

## Fixed Function 2
public void removeObject(Comparable rowKey, Comparable columnKey) {
   int rowIndex = getRowIndex(rowKey);
   KeyedObjects row = (KeyedObjects) this.rows.get(rowIndex);
   row.removeValue(columnKey);
   if (row.getItemCount() == 0) {
      this.rowKeys.remove(rowIndex);
      this.rows.remove(rowIndex);
   }
}

## Fixed Function 3
public void removeRow(Comparable rowKey) {
   int index = getRowIndex(rowKey);
   removeRow(index);
}

## Fixed Function 4
public void removeColumn(Comparable columnKey) {
   int index = getColumnIndex(columnKey);
   if (index < 0) {
      throw new UnknownKeyException("Column key (" + columnKey + ") not recognised.");
   }
   for (int i = 0; i < this.rows.size(); i++) {
      KeyedObjects rowData = (KeyedObjects) this.rows.get(i);
      rowData.removeValue(columnKey);
      if (rowData.getItemCount() == 0) {
         this.rowKeys.remove(i);
         this.rows.remove(i);
         i--;
      }
   }
   this.columnKeys.remove(columnKey);
}