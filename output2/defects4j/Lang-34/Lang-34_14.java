## Fixed Function 1
static Map<Object, Object> getRegistry() {
    Map<Object, Object> registry = REGISTRY.get();
    return (registry != null) ? registry : Collections.emptyMap();
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    return m.containsKey(value);
}