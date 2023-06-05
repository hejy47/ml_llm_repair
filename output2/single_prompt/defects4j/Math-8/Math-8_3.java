## Fixed Function 1
public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);
    RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    for (int i = 0; i < sampleSize; i++) {
        out[i] = singletons.get(randomDataGenerator.nextInt(0, singletons.size() - 1));
    }
    return out;
}