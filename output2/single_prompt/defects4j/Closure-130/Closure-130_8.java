private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }
        if (name.globalSets == 1 && name.localSets == 0 && name.aliasingGets > 0) {
            List<Ref> refs = Lists.newArrayList(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    if (inlineAliasIfPossible(ref, namespace)) {
                        name.removeRef(ref);
                        // Remove the local reference from the worklist to avoid processing it again
                        workList.remove(ref.node.getName());
                    }
                }
            }
        }
        if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) && name.aliasingGets == 0 && name.props != null) {
            workList.addAll(name.props);
        }
    }
}