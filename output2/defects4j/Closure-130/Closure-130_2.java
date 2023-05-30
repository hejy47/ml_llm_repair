private void inlineAliases(GlobalNamespace namespace) {
    // Invariant: All the names in the worklist meet condition (a).
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();
        // Don't attempt to inline a getter or setter property as a variable.
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }
        if (name.globalSets == 1 && name.localSets == 0 && name.aliasingGets > 0) {
            // {@code name} meets condition (b).
            List<Ref> aliasingRefs = name.getRefs(Type.ALIASING_GET);
            // Only proceed if there is exactly one local alias
            if (aliasingRefs.size() == 1 && aliasingRefs.get(0).scope.isLocal()) {
                Ref ref = aliasingRefs.get(0);
                // {@code name} meets condition (c).
                if (inlineAliasIfPossible(ref, namespace)) {
                    name.removeRef(ref);
                }
            }
        }
        // Check if {@code name} has any aliases left after the
        // local-alias-inlining above.
        if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) && name.aliasingGets == 0 && name.props != null) {
            // All of {@code name}'s children meet condition (a), so they can be
            // added to the worklist.
            workList.addAll(name.props);
        }
    }
}