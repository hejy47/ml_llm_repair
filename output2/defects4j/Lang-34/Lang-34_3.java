## Fixed Function 1
static Map<Object, Object> getRegistry() {
    return REGISTRY.get() != null ? REGISTRY.get() : Collections.synchronizedMap(new HashMap<Object, Object>());
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    synchronized (m) {
        return m.containsKey(value);
    }
}