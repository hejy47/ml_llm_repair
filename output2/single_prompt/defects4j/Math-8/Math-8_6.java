## Fixed Function 1
public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);
    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }
    return Arrays.copyOf(out, out.length);
}

The fix is to use the Arrays.copyOf() method to create a new array with the same contents as the original array. This ensures that the returned array is independent of the original array and any modifications to it will not affect the original.