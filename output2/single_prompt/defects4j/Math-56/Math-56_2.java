## Fixed Function 1
public int[] getCounts(int index) {
    if (index < 0 || index >= getTotalSize()) {
        throw new OutOfRangeException(index, 0, getTotalSize());
    }
    final int[] indices = new int[getDimension()];
    int count = 0;
    for (int i = 0; i < getLast(); i++) {
        int idx = 0;
        final int offset = getUniCounterOffset(i);
        while (count + offset <= index) {
            count += offset;
            ++idx;
        }
        indices[i] = idx - 1;
    }
    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    indices[getLast()] = idx - 1;
    return indices;
}