## Fixed Function 1
static Map<Object, Object> getRegistry() {
    if (REGISTRY.get() != null) {
        return REGISTRY.get();
    } else {
        REGISTRY.set(new HashMap<>());
        return REGISTRY.get();
    }
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    return m != null && m.containsKey(value);
}