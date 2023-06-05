## Fixed Function 1
@Override
protected Set<IDKey> initialValue() {
    return Collections.synchronizedSet(new HashSet<IDKey>());
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    synchronized (LOCK) {
        return getRegistry().contains(new IDKey(value));
    }
}

## Fixed Function 3
static void unregister(Object value) {
    synchronized (LOCK) {
        getRegistry().remove(new IDKey(value));
    }
}