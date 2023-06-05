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
                // Check if either operand is UNKNOWN before getting their values
                TernaryValue lhs = getImpureBooleanValue(n.getFirstChild());
                if (lhs == TernaryValue.UNKNOWN) {
                    return TernaryValue.UNKNOWN;
                }
                TernaryValue rhs = getImpureBooleanValue(n.getLastChild());
                if (rhs == TernaryValue.UNKNOWN) {
                    return TernaryValue.UNKNOWN;
                }
                return lhs.and(rhs);
            }
        case Token.OR:
            {
                // Check if either operand is UNKNOWN before getting their values
                TernaryValue lhs = getImpureBooleanValue(n.getFirstChild());
                if (lhs == TernaryValue.UNKNOWN) {
                    return TernaryValue.UNKNOWN;
                }
                TernaryValue rhs = getImpureBooleanValue(n.getLastChild());
                if (rhs == TernaryValue.UNKNOWN) {
                    return TernaryValue.UNKNOWN;
                }
                return lhs.or(rhs);
            }
        case Token.HOOK:
            {
                TernaryValue trueValue = getImpureBooleanValue(n.getFirstChild().getNext());
                TernaryValue falseValue = getImpureBooleanValue(n.getLastChild());
                // Check if either operand is UNKNOWN before comparing them
                if (trueValue == TernaryValue.UNKNOWN || falseValue == TernaryValue.UNKNOWN) {
                    return TernaryValue.UNKNOWN;
                }
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
            // Check if the operand is UNKNOWN before getting its value
            TernaryValue operand = getPureBooleanValue(n.getLastChild());
            if (operand == TernaryValue.UNKNOWN) {
                return TernaryValue.UNKNOWN;
            }
            return operand.not();
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
            if (!mayHaveSideEffects(n)) {
                return TernaryValue.TRUE;
            }
            break;
    }
    return TernaryValue.UNKNOWN;
}