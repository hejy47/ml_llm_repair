## Fixed Function 1
@Deprecated
public void addValue(Object v) {
    Object obj = v;
    if (v instanceof Integer) {
        obj = Long.valueOf(((Integer) v).longValue());
    }
    try {
        if (obj instanceof Comparable) { // check if obj is comparable
            Long count = (Long) freqTable.get(obj);
            if (count == null) {
                freqTable.put(obj, Long.valueOf(1));
            } else {
                freqTable.put(obj, Long.valueOf(count.longValue() + 1));
            }
        } else {
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    } catch (ClassCastException ex) {
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}