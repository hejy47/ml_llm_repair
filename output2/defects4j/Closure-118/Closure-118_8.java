private void handleObjectLit(NodeTraversal t, Node n) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (child.isString()) {
            String name = child.getString();
            T type = typeSystem.getType(getScope(), n, name);
            Property prop = getProperty(name);
            if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
                // TODO(user): It doesn't look like the user can do much in this
                // case right now.
                if (propertiesToErrorFor.containsKey(name)) {
                    compiler.report(JSError.make(t.getSourceName(), child, propertiesToErrorFor.get(name), Warnings.INVALIDATION, name, (type == null ? "null" : type.toString()), n.toString(), ""));
                }
            }
        } else {
            // Throw an error when object literal contains mix of numbers and strings.
            compiler.report(JSError.make(t.getSourceName(), child, Warnings.INVALID_OBJECT_LITERAL_MIX));
        }
    }
}