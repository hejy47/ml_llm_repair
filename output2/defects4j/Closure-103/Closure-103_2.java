## Fixed Function 1
private static boolean mayThrowException(Node n) {
    switch(n.getType()) {
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
        case Token.BLOCK:
        case Token.SCRIPT:
        case Token.CATCH:
            // these types don't throw exceptions themselves, but their children might
            for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                if (mayThrowException(c)) {
                    return true;
                }
            }
            return false;
        default:
            return false;
    }
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
    // this appears.  This will make references to overriden properties look
    // like references to the initial property, so they are renamed alike.
    ObjectType foundType = null;
    ObjectType objType = ObjectType.cast(type);
    while (objType != null && objType.getImplicitPrototype() != objType) {
        if (objType.hasOwnProperty(field)) {
            foundType = objType;
            break; // exit loop after property is found
        }
        objType = objType.getImplicitPrototype();
    }
    // If the property does not exist on the referenced type but the original
    // type is an object type, see if any subtype has the property.
    // getGreatestSubtypeWithProperty does not guarantee that the property
    // is defined on the returned type, it just indicates that it might be,
    // so we have to double check.
    if (foundType == null) {
        ObjectType greatestSubtypeWithProperty =
                objType.getGreatestSubtypeWithProperty(field);
        if (greatestSubtypeWithProperty != null &&
                greatestSubtypeWithProperty.hasOwnProperty(field)) {
            foundType = greatestSubtypeWithProperty;
        }
    }
    return foundType;
}