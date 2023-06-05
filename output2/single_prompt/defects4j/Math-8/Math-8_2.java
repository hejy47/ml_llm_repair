## Fixed Function 1
public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);
    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }
    return out.clone(); // Added clone() method to return a new array
}