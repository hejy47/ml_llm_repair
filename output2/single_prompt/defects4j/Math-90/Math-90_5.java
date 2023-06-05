## Fixed Function 1
@Deprecated
public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }

    Comparable obj = (Comparable) v;
    if (v instanceof Integer) {
        obj = Long.valueOf(((Integer) v).longValue());
    }

    Long count = freqTable.get(obj);
    if (count == null) {
        freqTable.put(obj, Long.valueOf(1));
    } else {
        freqTable.put(obj, Long.valueOf(count.longValue() + 1));
    }
}