## Fixed Function 1
public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }
    final T[] out = Arrays.copyOf(singletonList.toArray(), sampleSize);
    for (int i = singletonList.size(); i < sampleSize; i++) {
        out[i] = sample();
    }
    return out;
}