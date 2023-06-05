## Fixed Function 1
@Deprecated
public <T extends Comparable<T>> void addValue(T v) {
    try {
        Long count = (Long) freqTable.get(v);
        if (count == null) {
            freqTable.put(v, Long.valueOf(1));
        } else {
            freqTable.put(v, Long.valueOf(count.longValue() + 1));
        }
    } catch (ClassCastException ex) {
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}