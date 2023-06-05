## Fixed Function 1
@Deprecated
public void addValue(Object v) {
    Object obj = v;
    if (v instanceof Integer) {
        obj = Long.valueOf(((Integer) v).longValue());
    }
    try {
        Long count = freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            freqTable.put(obj, Long.valueOf(count.longValue() + 1));
        }
    } catch (ClassCastException ex) {
        //TreeMap will throw ClassCastException if v is not comparable
        throw new IllegalArgumentException("Value not comparable to existing values.");
    } catch (NullPointerException ex) {
        //freqTable may be null
        throw new IllegalArgumentException("freqTable is null.");
    }
} 

## Explanation
The `NullPointerException` may occur when accessing an instance of `Map` which is not constructed yet. In the new implementation, we catch the `NullPointerException` and throw an exception to notify that the `freqTable` is null.