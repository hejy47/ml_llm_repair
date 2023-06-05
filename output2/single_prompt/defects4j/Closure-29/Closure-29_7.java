private boolean isInlinableObject(List<Reference> refs) {
    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node gramps = ref.getGrandparent();
        // Ignore indirect references, like x.y (except x.y(), since
        // the function referenced by y might reference 'this').
        //
        if (parent.isGetProp()) {
            Preconditions.checkState(parent.getFirstChild() == name);
            // A call target maybe using the object as a 'this' value.
            if (gramps.isCall() && gramps.getFirstChild() == parent) {
                return false;
            }
            // To fix the blind spot problem, confirm that names in property
            // accesses are only literal properties.
            Node obj = parent.getFirstChild();
            Node prop = obj.getNext();
            if (obj.isQualifiedName()) {
                // Reject all qualified names.
                return false;
            } else if (obj.isThis()) {
                // Allow 'this.prop', since it can't be Object.prototype.
            } else if (obj.isName()) {
                if (!isDefinitelyObject(obj)) {
                    return false;
                }
            } else {
                throw new RuntimeException("Unexpected node type");
            }
            if (prop.isString()) {
                String pname = prop.getString();
                if (NodeUtil.getObjectLiteralKeyName(pname) == null) {
                    return false;
                }
            } else {
                Preconditions.checkState(prop.isNumber());
            }
        } else {
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
                for (Reference t : refs) {
                    Node refNode = t.getParent();
                    while (!NodeUtil.isStatementBlock(refNode)) {
                        if (refNode == childVal) {
                            // There's a self-referential assignment
                            return false;
                        }
                        refNode = refNode.getParent();
                    }
                }
            }
        }
    }
    return true;
}