## Fixed Function 1
static boolean isValidDefineValue(Node val, Set<String> defines) {
    switch(val.getType()) {
        case Token.STRING:
        case Token.NUMBER:
        case Token.TRUE:
        case Token.FALSE:
            return true;
        // Binary operators are only valid if both children are valid.
        case Token.BITAND:
        case Token.BITNOT:
        case Token.BITOR:
        case Token.BITXOR:
            // Fixed: check both left and right children for validity
            return isValidDefineValue(val.getFirstChild(), defines) && isValidDefineValue(val.getLastChild(), defines);
        // Uniary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines);
        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
            if (val.isName() && defines.contains(val.getString())) {
                return true;
            }
            break;
        case Token.GETPROP:
            if (val.isGetProp() && val.getLastChild().isString()) {
                String propName = val.getLastChild().getString();
                Node obj = val.getFirstChild();
                if (obj.isName() && defines.contains(obj.getString() + "." + propName)) {
                    return true;
                }
            }
            break;
    }
    return false;
}