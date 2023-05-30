## Fixed Function 1
@Override
protected Set<IDKey> initialValue() {
    return new HashSet<>();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    IDKey key = new IDKey(value);
    return getRegistry().contains(key);
}

## Fixed Function 3
static void unregister(Object value) {
    IDKey key = new IDKey(value);
    getRegistry().remove(key);
}