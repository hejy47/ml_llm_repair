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
        case Token.REGEXP:
            return true;
        case Token.FUNCTION:
        case Token.EQ:
        case Token.NE:
        case Token.LE:
        case Token.LT:
        case Token.GE:
        case Token.GT:
        case Token.INSTANCEOF:
        case Token.IN:
        case Token.OR:
        case Token.AND:
        case Token.CONDITIONAL:
        case Token.HOOK:
        case Token.NAME:
        case Token.NUMBER:
        case Token.STRING:
        case Token.SWITCH:
        case Token.CASE:
        case Token.DEFAULT:
        case Token.RETURN:
        case Token.VOID:
        case Token.NEW_WITH_ARGS:
        case Token.DELPROP:
        case Token.TYPEOF:
        case Token.ADD:
        case Token.SUB:
        case Token.MUL:
        case Token.MOD:
        case Token.DIV:
        case Token.BITNOT:
        case Token.NOT:
        case Token.POS:
        case Token.NEG:
        case Token.BITOR:
        case Token.BITXOR:
        case Token.BITAND:
        case Token.LSH:
        case Token.RSH:
        case Token.URSH:
        case Token.EMPTY:
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
    while (objType != null && objType.getImplicitPrototype() != objType) {
        if (objType.hasOwnProperty(field)) {
            foundType = objType;
            break;
        }
        objType = objType.getImplicitPrototype();
    }
    // If the property does not exist on the referenced type but the original
    // type is an object type, see if any subtype has the property.
    // getGreatestSubtypeWithProperty does not guarantee that the property
    // is defined on the returned type, it just indicates that it might be,
    // so we have to double check.
    if (foundType == null && type.getObjectType() != null) {
        ObjectType obj = type.getObjectType().getGreatestSubtypeWithProperty(field);
        if (obj != null && obj.hasOwnProperty(field)) {
            foundType = obj;
        }
    }
    return foundType;
}