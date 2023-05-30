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
            // Uniary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines) 
                    && isValidDefineValue(val.getLastChild(), defines); //Fix: Added check for last child of binary operator
        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
        case Token.GETPROP:
            if (val.isQualifiedName()) {
                return defines.contains(val.getQualifiedName());
            }
    }
    return false;
}