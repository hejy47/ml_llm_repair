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
            // We need to check if both children are valid.
            return isValidDefineValue(val.getFirstChild(), defines) && isValidDefineValue(val.getLastChild(), defines);
        // Unary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines);
        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
        case Token.GETPROP:
            if (val.isQualifiedName()) {
                return defines.contains(val.getQualifiedName());
            }
    }
    return false;
}