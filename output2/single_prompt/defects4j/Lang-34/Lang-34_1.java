## Fixed Function 1
static Map<Object, Object> getRegistry() {
    if (REGISTRY.get() == null) {
        REGISTRY.set(Collections.synchronizedMap(new HashMap<Object, Object>()));
    }
    return REGISTRY.get();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    synchronized(m) {
        return m.containsKey(value);
    }
}