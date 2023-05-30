## Fixed Function 1
public int[] getCounts(int index) {
    if (index < 0 || index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }
    final int[] indices = new int[dimension];
    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count + offset <= index) {  // fixed condition in while loop
            count += offset;
            ++idx;
        }
        indices[i] = idx - 1;  // subtract 1 from idx to properly index the array
    }
    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    indices[last] = idx - 1;  // subtract 1 from idx to properly index the array
    return indices;
}