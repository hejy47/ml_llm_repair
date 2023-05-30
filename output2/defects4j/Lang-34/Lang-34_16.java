## Fixed Function 1
static Map<Object, Object> getRegistry() {
    return REGISTRY.get() != null ? REGISTRY.get() : Collections.emptyMap();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    return m != null && m.containsKey(value);
}