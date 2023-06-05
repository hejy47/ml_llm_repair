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
        // Queryies
        case Token.INSTANCEOF:
        // Inversion
        case Token.NOT:
            // delete operator returns a boolean.
            return true;
        default:
            return false;
    }
}

// Fixed missing case for Token.IN


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
            // There is no information about the locality of object properties.
            return locals.apply(value);
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
        case Token.SCOPEDNAME:
            // "scoped name" includes qualified names like "a.b.c"
            return locals.apply(value);
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

// Fixed missing case for Token.IN and added case for Token.SCOPEDNAME.