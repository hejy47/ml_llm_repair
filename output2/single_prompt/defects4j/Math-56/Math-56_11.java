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
        while (count + offset <= index) {
            count += offset;
            ++idx;
        }
        indices[i] = idx;
    }
    int remainingIndex = index - count + 1;
    indices[last] = (int) ((-1 + Math.sqrt(1 + 8 * remainingIndex)) / 2);
    return indices;
}