static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
    switch(value.getType()) {
        case Token.ASSIGN:
            return NodeUtil.isImmutableValue(value.getLastChild()) || (locals.apply(value.getLastChild()) && evaluatesToLocalValue(value.getLastChild(), locals));
        case Token.COMMA:
            return evaluatesToLocalValue(value.getLastChild(), locals);
        case Token.AND:
        case Token.OR:
            return evaluatesToLocalValue(value.getFirstChild(), locals) && evaluatesToLocalValue(value.getLastChild(), locals);
        case Token.HOOK:
            return evaluatesToLocalValue(value.getFirstChild().getNext(), locals) && evaluatesToLocalValue(value.getLastChild(), locals);
        case Token.INC:
        case Token.DEC:
            if (value.getBooleanProp(Node.INCRDECR_PROP)) {
                return evaluatesToLocalValue(value.getFirstChild(), locals);
            } else {
                return true;
            }
        case Token.THIS:
            return locals.apply(value);
        case Token.NAME:
            return isImmutableValue(value) || locals.apply(value);
        case Token.GETELEM:
        case Token.GETPROP:
            // There is no information about the locality of object properties,
            // so we assume that the property value is not a local value.
            return false;
        case Token.CALL:
            return callHasLocalResult(value) || isToStringMethodCall(value) || locals.apply(value);
        case Token.NEW:
            // Similarly to CALL, we assume that constructor arguments are not local,
            // but the result of the constructor is a local value if it is assigned to a local name.
            return locals.apply(value.getParent());
        case Token.FUNCTION:
        case Token.REGEXP:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            // Literals objects with non-literal children are allowed.
            return true;
        case Token.IN:
            // IN operator is not a simple operator, so we need to check it separately.
            return evaluatesToLocalValue(value.getFirstChild(), locals)
                && evaluatesToLocalValue(value.getLastChild(), locals);
        default:
            // Other op force a local value:
            //  x = '' + g (x is now an local string)
            //  x -= g (x is now an local number)
            if (isAssignmentOp(value) || isSimpleOperator(value) || isImmutableValue(value)) {
                return true;
            }
            throw new IllegalStateException("Unexpected expression node" + value + "\n parent:" + value.getParent());
    }
}