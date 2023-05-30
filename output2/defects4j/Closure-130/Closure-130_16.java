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
            // {@code name} meets condition (b). Find all of its local aliases
            // and try to inline them.
            List<Ref> refs = Lists.newArrayList(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    // {@code name} meets condition (c).
                    // Fixed: Don't remove the ref in case inlining fails.
                    inlineAliasIfPossible(ref, namespace);
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