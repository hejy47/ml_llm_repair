## Fixed Function 1
static TernaryValue getImpureBooleanValue(Node n) {
    switch(n.getType()) {
        case Token.ASSIGN:
        case Token.COMMA:
            // For ASSIGN and COMMA the value is the value of the RHS.
            return getImpureBooleanValue(n.getLastChild());
        case Token.NOT:
            TernaryValue value = getImpureBooleanValue(n.getLastChild());
            return value.not();
        case Token.AND:
            {
                TernaryValue lhs = getImpureBooleanValue(n.getFirstChild());
                TernaryValue rhs = getImpureBooleanValue(n.getLastChild());
                return lhs.and(rhs);
            }
        case Token.OR:
            {
                TernaryValue lhs = getImpureBooleanValue(n.getFirstChild());
                TernaryValue rhs = getImpureBooleanValue(n.getLastChild());
                return lhs.or(rhs);
            }
        case Token.HOOK:
            {
                TernaryValue trueValue = getImpureBooleanValue(n.getFirstChild().getNext());
                TernaryValue falseValue = getImpureBooleanValue(n.getLastChild());
                if (trueValue.equals(falseValue)) {
                    return trueValue;
                } else {
                    return TernaryValue.UNKNOWN;
                }
            }
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            // ignoring side-effects
            return TernaryValue.TRUE;
        case Token.VOID:
        case Token.NEW:
        case Token.DELPROP:
        case Token.CALL:
        case Token.TYPEOF:
        case Token.POS:
        case Token.NEG:
        case Token.INC:
        case Token.DEC:
        case Token.BITOR:
        case Token.BITXOR:
        case Token.BITAND:
        case Token.LSH:
        case Token.RSH:
        case Token.URSH:
        case Token.ADD:
        case Token.SUB:
        case Token.MUL:
        case Token.DIV:
        case Token.MOD:
        case Token.EQ:
        case Token.NE:
        case Token.SHEQ:
        case Token.SHNE:
        case Token.LT:
        case Token.LE:
        case Token.GE:
        case Token.GT:
        case Token.INSTANCEOF:
        case Token.IN:
        case Token.GETPROP:
        case Token.GETELEM:
        case Token.CHECKCAST:
        case Token.INSTANCEOFEXPR:
            // These nodes have no impure boolean value.
            return TernaryValue.UNKNOWN;
        default:
            return getPureBooleanValue(n);
    }
}

## Fixed Function 2
static TernaryValue getPureBooleanValue(Node n) {
    switch(n.getType()) {
        case Token.STRING:
            return TernaryValue.forBoolean(n.getString().length() > 0);
        case Token.NUMBER:
            return TernaryValue.forBoolean(n.getDouble() != 0);
        case Token.NOT:
            return getPureBooleanValue(n.getLastChild()).not();
        case Token.NULL:
        case Token.FALSE:
            return TernaryValue.FALSE;
        case Token.VOID:
            return TernaryValue.FALSE;
        case Token.NAME:
            String name = n.getString();
            if ("undefined".equals(name) || "NaN".equals(name)) {
                // We assume here that programs don't change the value of the keyword
                // undefined to something other than the value undefined.
                return TernaryValue.FALSE;
            } else if ("Infinity".equals(name)) {
                return TernaryValue.TRUE;
            }
            break;
        case Token.TRUE:
        case Token.REGEXP:
            return TernaryValue.TRUE;
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            //checking for side-effect free expression
            if (NodeUtil.mayHaveSideEffects(n)) {
                return TernaryValue.FALSE;
            } else {
                return TernaryValue.TRUE;
            }
        default:
            return TernaryValue.UNKNOWN;
    }
}