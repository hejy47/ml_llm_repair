## Fixed Function 1
public int[] getCounts(int index) {
    if (index < 0 || index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize-1);
    }
    final int[] indices = new int[dimension];
    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count + offset <= index) { // fixed comparison here
            count += offset;
            ++idx;
        }
        indices[i] = idx - 1; // moved decrement outside the while loop
    }
    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    indices[last] = idx - 1; // moved decrement outside the while loop
    return indices;
}