## Fixed Function 1
static boolean isBooleanResultHelper(Node n) {
    switch(n.getType()) {
        // Primitives
        case Token.TRUE:
        case Token.FALSE:
        // Comparisons
        case Token.EQ:
        case Token.NE:
        case Token.SHEQ:
        case Token.SHNE:
        case Token.LT:
        case Token.GT:
        case Token.LE:
        case Token.GE:
        // Queries
        case Token.IN:
        case Token.INSTANCEOF:
        // Inversion
        case Token.NOT:
            // delete operator returns a boolean.
            return true;
        // added case for Token.TYPEOF
        case Token.TYPEOF:
            return true;
        default:
            return false;
    }
}

## Fixed Function 2
static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
    switch(value.getType()) {
        case Token.ASSIGN:
            // A result that is aliased by a non-local name, is the effectively the
            // same as returning a non-local name, but this doesn't matter if the
            // value is immutable.
            return NodeUtil.isImmutableValue(value.getLastChild()) || (locals.apply(value.getFirstChild()) && evaluatesToLocalValue(value.getLastChild(), locals));
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
            return NodeUtil.isImmutableValue(value) || locals.apply(value);
        case Token.GETELEM:
        case Token.GETPROP:
            // There is no information about the locality of object properties.
            return locals.apply(value.getFirstChild());
        case Token.CALL:
            return callHasLocalResult(value) || isToStringMethodCall(value) || locals.apply(value);
        case Token.NEW:
            return newHasLocalResult(value) || locals.apply(value);
        case Token.FUNCTION:
        case Token.REGEXP:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            // Literals objects with non-literal children are allowed.
            return true;
        case Token.IN:
            // TODO(johnlenz): should IN operator be included in #isSimpleOperator?
            return true;
        default:
            // Other ops force a local value:
            //  x = '' + g (x is now a local string)
            //  x -= g (x is now a local number)
            if (NodeUtil.isAssignmentOp(value) || NodeUtil.isSimpleOperator(value) || NodeUtil.isImmutableValue(value)) {
                return true;
            }
            throw new IllegalStateException("Unexpected expression node" + value);
    }
}