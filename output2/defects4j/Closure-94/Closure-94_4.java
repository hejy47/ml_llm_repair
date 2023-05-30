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
            // Check if both children are valid
            return isValidDefineValue(val.getFirstChild(), defines)
                    && isValidDefineValue(val.getLastChild(), defines);
        // Uniary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines);
        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
            if (val.isName()) {
                return defines.contains(val.getString());
            } else if (val.isGetProp()) {
                return defines.contains(val.getFirstChild().getQualifiedName());
            }
            break;
    }
    return false;
}