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
        while (count + offset <= index) { // fix 1: changing the condition to count + offset instead of count
            count += offset;
            ++idx;
        }
        indices[i] = idx - 1; // fix 2: subtracting 1 from idx since it's incremented at the end of each loop iteration
    }
    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    indices[last] = idx - 1; // fix 3: subtracting 1 from idx since it's incremented at the end of the while loop
    return indices;
}