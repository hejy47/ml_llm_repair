## Fixed Function 1
@Override
protected Set<IDKey> initialValue() {
    return new HashSet<>();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    return getRegistry().contains(new IDKey(value));
}

## Fixed Function 3
static void unregister(Object value) {
    Set<IDKey> registry = getRegistry();
    if (registry != null) {
        registry.remove(new IDKey(value));
    }
}