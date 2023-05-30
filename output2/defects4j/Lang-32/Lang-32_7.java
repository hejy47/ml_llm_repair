## Fixed Function 1
@Override
protected Set<IDKey> initialValue() {
    return new HashSet<>();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    return getRegistry().contains(new IDKey(System.identityHashCode(value)));
}

## Fixed Function 3
static void unregister(Object value) {
    getRegistry().remove(new IDKey(System.identityHashCode(value)));
}