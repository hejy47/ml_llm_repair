## Fixed Function 1
private static boolean mayThrowException(Node n) {
    switch(n.getToken()) { // Use getToken() instead of getType() to fix bug
        case Token.CALL:
        case Token.GETPROP:
        case Token.GETELEM:
        case Token.THROW:
        case Token.NEW:
        case Token.ASSIGN:
        case Token.INC:
        case Token.DEC:
            return true;
        case Token.FUNCTION:
            return false;
    }
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(c) && mayThrowException(c)) {
            return true;
        }
    }
    return false;
}

## Fixed Function 2
@Override
public ObjectType getTypeWithProperty(String field, JSType type) {
    if (!(type instanceof ObjectType)) {
        if (type.autoboxesTo() != null) {
            type = type.autoboxesTo();
        } else {
            return null;
        }
    }
    // Ignore the prototype itself at all times.
    if ("prototype".equals(field)) {
        return null;
    }
    // We look up the prototype chain to find the highest place (if any) that
    // this appears.  This will make references to overridden properties look
    // like references to the initial property, so they are renamed alike.
    ObjectType foundType = null;
    ObjectType objType = ObjectType.cast(type);
    while (objType != null && !objType.equals(objType.getImplicitPrototype())) { // Use equals() instead of != to fix bug
        if (objType.hasOwnProperty(field)) {
            foundType = objType;
        }
        objType = objType.getImplicitPrototype();
    }
    // If the property does not exist on the referenced type but the original
    // type is an object type, see if any subtype has the property.
    // getGreatestSubtypeWithProperty does not guarantee that the property
    // is defined on the returned type, it just indicates that it might be,
    // so we have to double check.
    if (foundType == null && type instanceof ObjectType) {
        foundType = ObjectType.cast(((ObjectType) type).getGreatestSubtypeWithProperty(field));
        if (foundType != null && !foundType.hasOwnProperty(field)) {
            foundType = null;
        }
    }
    return foundType;
}