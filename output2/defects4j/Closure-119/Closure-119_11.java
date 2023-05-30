public void collect(JSModule module, Scope scope, Node n) {
    Node parent = n.getParent();
    String name;
    boolean isSet = false;
    Name.Type type = Name.Type.OTHER;
    boolean isPropAssign = false;
    switch(n.getToken()) { // Changed from n.getType() to n.getToken()
        case GETTER_DEF: // Removed Token prefix from case statements
        case SETTER_DEF:
        case STRING_KEY:
            // This may be a key in an object literal declaration.
            name = null;
            if (parent != null && parent.isObjectLit()) {
                name = getNameForObjLitKey(n);
            }
            if (name == null) {
                return;
            }
            isSet = true;
            switch(n.getToken()) { // Changed from n.getType() to n.getToken()
                case STRING_KEY:
                    type = getValueType(n.getFirstChild());
                    break;
                case GETTER_DEF: // Removed Token prefix from case statements
                    type = Name.Type.GET;
                    break;
                case SETTER_DEF:
                    type = Name.Type.SET;
                    break;
                default:
                    throw new IllegalStateException("unexpected:" + n);
            }
            break;
        case NAME:
            // This may be a variable get or set.
            if (parent != null) {
                switch(parent.getToken()) { // Changed from parent.getType() to parent.getToken()
                    case VAR:
                        isSet = true;
                        Node rvalue = n.getFirstChild();
                        type = rvalue == null ? Name.Type.OTHER : getValueType(rvalue);
                        break;
                    case ASSIGN:
                        if (parent.getFirstChild() == n) {
                            isSet = true;
                            type = getValueType(n.getNext());
                        }
                        break;
                    case GETPROP:
                        return;
                    case FUNCTION:
                        Node gramps = parent.getParent();
                        if (gramps == null || NodeUtil.isFunctionExpression(parent)) {
                            return;
                        }
                        isSet = true;
                        type = Name.Type.FUNCTION;
                        break;
                    case INC:
                    case DEC:
                        isSet = true;
                        type = Name.Type.OTHER;
                        break;
                    default:
                        if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
                            isSet = true;
                            type = Name.Type.OTHER;
                        }
                }
            }
            name = n.getString();
            break;
        case GETPROP:
            // This may be a namespaced name get or set.
            if (parent != null) {
                switch(parent.getToken()) { // Changed from parent.getType() to parent.getToken()
                    case ASSIGN:
                        if (parent.getFirstChild() == n) {
                            isSet = true;
                            type = getValueType(n.getNext());
                            isPropAssign = true;
                        }
                        break;
                    case INC:
                    case DEC:
                        isSet = true;
                        type = Name.Type.OTHER;
                        break;
                    case GETPROP:
                        return;
                    default:
                        if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
                            isSet = true;
                            type = Name.Type.OTHER;
                        }
                }
            }
            name = n.getQualifiedName();
            if (name == null) {
                return;
            }
            break;
        default:
            return;
    }
    // We are only interested in global names.
    if (!isGlobalNameReference(name, scope)) {
        return;
    }
    if (isSet) {
        if (isGlobalScope(scope)) {
            handleSetFromGlobal(module, scope, n, parent, name, isPropAssign, type);
        } else {
            handleSetFromLocal(module, scope, n, parent, name);
        }
    } else {
        handleGet(module, scope, n, parent, name);
    }
}