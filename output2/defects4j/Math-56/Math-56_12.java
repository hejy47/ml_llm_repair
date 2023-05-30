## Fixed Function 1
public int[] getCounts(int index) {
    if (index < 0 || index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize-1); // fix: subtract 1 from totalSize
    }
    final int[] indices = new int[dimension];
    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count + offset <= index) { // fix: use "count + offset" instead of "count <= index"
            count += offset;
            ++idx;
        }
        indices[i] = idx - 1; // fix: decrement idx by 1
    }
    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    indices[last] = idx - 1; // fix: decrement idx by 1
    return indices;
}