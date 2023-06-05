## Fixed Function 1
@Deprecated
public void addValue(Object v) {
    if (v instanceof Comparable) { // Check if v is comparable
        Comparable obj = (Comparable) v;
        try {
            Long count = (Long) freqTable.get(obj);
            if (count == null) {
                freqTable.put(obj, Long.valueOf(1));
            } else {
                freqTable.put(obj, Long.valueOf(count.longValue() + 1));
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    } else {
        throw new IllegalArgumentException("Value not comparable to existing values."); // Throw exception if v is not comparable
    }
}