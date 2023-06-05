## Fixed Function 1
@Override
protected Set<IDKey> initialValue() {
    return new HashSet<>();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Set<IDKey> registry = getRegistry();
    synchronized (registry) {
        return registry.contains(new IDKey(value));
    }
}

## Fixed Function 3
static void unregister(Object value) {
    Set<IDKey> registry = getRegistry();
    synchronized (registry) {
        registry.remove(new IDKey(value));
    }
}