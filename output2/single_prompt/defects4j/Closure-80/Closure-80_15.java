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
            // Delete operator returns a boolean.
            // Void operator also returns a boolean, so include it here.
            // New operator returns an object, but it requires an error if it fails,
            // so include it here as well.
            // Typeof operator returns a string or undefined, but for the purposes of
            // this function, they are treated as a boolean value.
            case Token.VOID:
            case Token.NEW:
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
            return NodeUtil.isImmutableValue(value.getLastChild()) || (locals.apply(value) && evaluatesToLocalValue(value.getLastChild(), locals));
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
        case Token.NEW:
            // There is no information about the locality of object properties.
            // But new operator requires an error if it fails, so include it here.
            return locals.apply(value);
        case Token.CALL:
            return callHasLocalResult(value) || isToStringMethodCall(value) || locals.apply(value);
        case Token.FUNCTION:
        case Token.REGEXP:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            // Literal objects with non-literal children are allowed.
            return true;
        case Token.IN:
            // TODO(johnlenz): should IN operator be included in #isSimpleOperator?
            return true;
        default:
            // Other operators force a local value:
            //  x = '' + g (x is now a local string)
            //  x -= g (x is now a local number)
            //  No-value-returning expression such as `throw` and `debugger` also require a local
            //  value for control flow analysis.
            if (isAssignmentOp(value) || isSimpleOperator(value) || isImmutableValue(value)
                || isControlStructure(value)) {
                return true;
            }
            throw new IllegalStateException("Unexpected expression node" + value + "\n parent:" + value.getParent());
    }
}