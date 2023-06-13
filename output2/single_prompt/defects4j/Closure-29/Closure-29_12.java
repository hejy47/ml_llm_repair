private boolean isInlinableObject(List<Reference> refs) {
    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node grandparent = ref.getGrandparent();
        // Ignore indirect references, like x.y (except x.y(), since
        // the function referenced by y might reference 'this').
        //
        if (parent.isGetProp()) {
            Preconditions.checkState(parent.getFirstChild() == name);
            // A call target maybe using the object as a 'this' value.
            if (grandparent.isCall() && grandparent.getFirstChild() == parent) {
                return false;
            }
            // NOTE(nicksantos): This pass's object-splitting algorithm has
            // a blind spot. It assumes that if a property isn't defined on an
            // object, then the value is undefined. This is not true, because
            // Object.prototype can have arbitrary properties on it.
            //
            // We short-circuit this problem by bailing out if we see a reference
            // to a property that isn't defined on the object literal. This
            // isn't a perfect algorithm, but it should catch most cases.
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
        // Make sure that the value is not self-refential. IOW,
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
            Node childVal = child.getFirstChild();
            // Check if childVal is the parent of any of the passed in
            // references, as that is how self-referential assignments
            // will happen.
            for (Reference innerRef : refs) {
                Node innerRefNode = innerRef.getParent();
                while (!NodeUtil.isStatementBlock(innerRefNode)) {
                    if (innerRefNode == childVal) {
                        // There's a self-referential assignment
                        return false;
                    }
                    innerRefNode = innerRefNode.getParent();
                }
            }
        }
    }
    return true;
}