## Fixed Function 1
static Map<Object, Object> getRegistry() {
    return Optional.ofNullable(REGISTRY.get()).orElse(Collections.emptyMap());
}


## Fixed Function 2
static boolean isRegistered(Object value) {
    Map<Object, Object> m = getRegistry();
    return m.containsKey(value);
}