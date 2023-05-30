## Fixed Function 1
static Map<Object, Object> getRegistry() {
    Map<Object, Object> registry = REGISTRY.get();
    if (registry != null) {
        return registry;
    }
    return Collections.emptyMap();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    return m != null && m.containsKey(value);
}