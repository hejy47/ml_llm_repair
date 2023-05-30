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
        while (count + offset <= index) { //fix - checking if count+ offset is less than or equal to index
            count += offset;
            ++idx;
        }
        indices[i] = idx - 1; //fix - subtracting 1 from idx to account for the while loop adding 1 extra value
    }
    indices[last] = index - count; //fix - calculating remaining count after loop and assigning to last index
    return indices;
}