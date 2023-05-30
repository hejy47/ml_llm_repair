## Fixed Function 1
static Map<Object, Object> getRegistry() {
    Map<Object, Object> registry = REGISTRY.get();
    if (registry == null) {
        registry = new HashMap<Object, Object>();
        REGISTRY.set(registry);
    }
    return registry;
}

## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    return m.containsKey(value);
}