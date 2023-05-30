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
            freqTable.put(obj, count + 1);
        }
    } catch (ClassCastException ex) {
        //TreeMap will throw ClassCastException if v is not comparable
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}

## Changes Made
- We simplified the logic and removed unnecessary casting of primitives.
- We have removed the unnecessary wrapper function calls.