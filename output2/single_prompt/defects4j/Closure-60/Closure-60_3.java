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
                    // Check if both values are TRUE, return UNKNOWN instead of TRUE
                    TernaryValue and = trueValue.and(falseValue);
                    if (and == TernaryValue.TRUE) {
                        return TernaryValue.UNKNOWN;
                    } else {
                        return and;
                    }
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
            // The function mayHaveSideEffects() was missing, let's add it here
            if (!mayHaveSideEffects(n)) {
                return TernaryValue.TRUE;
            }
            break;
    }
    return TernaryValue.UNKNOWN;
}

static boolean mayHaveSideEffects(Node n) {
    // Check for side-effect-free nodes
    switch(n.getType()) {
        case Token.TRUE:
        case Token.FALSE:
        case Token.NULL:
        case Token.NUMBER:
        case Token.STRING:
            return false;
        case Token.NAME:
            String name = n.getString();
            return !"undefined".equals(name) && !"NaN".equals(name) && !"Infinity".equals(name);
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
                if (mayHaveSideEffects(child)) {
                    return true;
                }
            }
            return false;
        default:
            return true;
    }
}