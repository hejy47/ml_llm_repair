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
        case Token.CATCH:
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
    // this appears.  This will make references to overriden properties look
    // like references to the initial property, so they are renamed alike.
    ObjectType foundType = null;
    ObjectType objType = ObjectType.cast(type);
    while (objType != null && foundType == null && objType.getImplicitPrototype() != objType) {
        if (objType.hasOwnProperty(field)) {
            foundType = objType;
        }
        objType = objType.getImplicitPrototype();
    }
    if (foundType == null && objType instanceof FunctionType) {
        // If the property does not exist on the referenced type but the original
        // type is a function type, we try to infer the type of the parameter.
        // For example, if the function type represents a jQuery plugin, we assume
        // the first parameter is a DOM element and return the object type of that.
        foundType = CTypeUtils.getFromFunction((FunctionType) objType, 0);
    }
    return foundType;
}