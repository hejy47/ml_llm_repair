## Fixed Function 1
@Deprecated
public void addValue(Object v) {
    Object obj = v;
    if (v instanceof Integer) {
        obj = Long.valueOf(((Integer) v).longValue());
    } else if (!(v instanceof Long)) { // Only comparable objects should be added
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
    Long count = (Long) freqTable.get(obj);
    if (count == null) {
        freqTable.put(obj, Long.valueOf(1));
    } else {
        freqTable.put(obj, Long.valueOf(count.longValue() + 1));
    }
}