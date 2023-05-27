private boolean isInlinableObject(List<Reference> refs) {
    Set<String> validProperties = Sets.newHashSet();
    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node gramps = ref.getGrandparent();
        // Ignore most indirect references, like x.y (but not x.y(),
        // since the function referenced by y might reference 'this').
        //
        if (parent.isGetProp()) {
            Preconditions.checkState(parent.getFirstChild() == name);
            // A call target may be using the object as a 'this' value.
            if (gramps.isCall() && gramps.getFirstChild() == parent) {
                return false;
            }
            // Deleting a property has different semantics from deleting
            // a variable, so deleted properties should not be inlined.
            // NOTE(nicksantos): This pass's object-splitting algorithm has
            // a blind spot. It assumes that if a property isn't defined on an
            // object, then the value is undefined. This is not true, because
            // Object.prototype can have arbitrary properties on it.
            //
            // We short-circuit this problem by bailing out if we see a reference
            // to a property that isn't defined on the object literal. This
            // isn't a perfect algorithm, but it should catch most cases.
            String propName = parent.getLastChild().getString();
            if (!validProperties.contains(propName)) {
                if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
                    validProperties.add(propName);
                } else {
                    return false;
                }
            }
            continue;
        }
        // Only rewrite VAR declarations or simple assignment statements
        if (!isVarOrAssignExprLhs(name)) {
            return false;
        }
        Node val = ref.getAssignedValue();
        if (val == null) {
            // A var with no assignment.
            continue;
        }
        // We're looking for object literal assignments only.
        if (!val.isObjectLit()) {
            return false;
        }
        // Make sure that the value is not self-referential. IOW,
        // disallow things like x = {b: x.a}.
        //
        // TODO: Only exclude unorderable self-referential
        // assignments. i.e. x = {a: x.b, b: x.a} is not orderable,
        // but x = {a: 1, b: x.a} is.
        //
        // Also, ES5 getters/setters aren't handled by this pass.
        for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isGetterDef() || child.isSetterDef()) {
                // ES5 get/set not supported.
                return false;
            }
            validProperties.add(child.getString());
            Node childVal = child.getFirstChild();
            // Check if childVal is the parent of any of the passed in
            // references, as that is how self-referential assignments
            // will happen.
            for (Reference t : refs) {
                // only check references that are not the declaring reference
                if (t == ref) {
                    continue;
                }
                Node refNode = t.getNode();
                while (!NodeUtil.isStatementBlock(refNode)) {
                    if (refNode == childVal) {
                        // There's a self-referential assignment
                        return false;
                    }
                    refNode = refNode.getParent();
                }
            }
        }
        // We have found an acceptable object literal assignment. As
        // long as there are no other assignments that mess things up,
        // we can inline.
        return true;
    }
    return false;
}