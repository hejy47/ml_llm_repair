@SuppressWarnings("fallthrough")
private Node tryFoldComparison(Node n, Node left, Node right) {
    if (!NodeUtil.isLiteralValue(left, false) || !NodeUtil.isLiteralValue(right, false)) {
        // We only handle non-literal operands for LT and GT.
        if (n.getType() != Token.GT && n.getType() != Token.LT) {
            return n;
        }
    }
    int op = n.getType();
    boolean result;
    // TODO(johnlenz): Use the JSType to compare nodes of different types.
    boolean rightLiteral = NodeUtil.isLiteralValue(right, false);
    boolean undefinedRight = ((Token.NAME == right.getType() && right.getString().equals("undefined")) || (Token.VOID == right.getType() && NodeUtil.isLiteralValue(right.getFirstChild(), false)));
    int lhType = left.getType();
    int rhType = right.getType();
    switch (lhType) {
        case Token.VOID:
            if (!NodeUtil.isLiteralValue(left.getFirstChild(), false)) {
                return n;
            } else if (!rightLiteral) {
                return n;
            } else {
                result = compareToUndefined(right, op);
            }
            break;
        case Token.NULL:
        case Token.TRUE:
        case Token.FALSE:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (rhType != Token.TRUE && rhType != Token.FALSE && rhType != Token.NULL) {
                return n;
            }
            switch (op) {
                case Token.SHEQ:
                case Token.EQ:
                    result = lhType == rhType;
                    break;
                case Token.SHNE:
                case Token.NE:
                    result = lhType != rhType;
                    break;
                case Token.GE:
                case Token.LE:
                case Token.GT:
                case Token.LT:
                    // Only compare numbers if both operands are numbers
                    if (lhType != rhType || lhType != Token.NUMBER) {
                        return n;
                    }
                    Boolean compareResult = compareAsNumbers(op, left, right);
                    if (compareResult != null) {
                        result = compareResult;
                    } else {
                        return n;
                    }
                    break;
                default:
                    // we only handle == and != here
                    return n;
            }
            break;
        case Token.THIS:
            if (right.getType() != Token.THIS) {
                return n;
            }
            switch (op) {
                case Token.SHEQ:
                case Token.EQ:
                    result = true;
                    break;
                case Token.SHNE:
                case Token.NE:
                    result = false;
                    break;
                // We can only handle == and != here.
                // GT, LT, GE, LE depend on the type of "this" and how it will
                // be converted to number.  The results are different depending on
                // whether it is a string, NaN or other number value.
                default:
                    return n;
            }
            break;
        case Token.STRING:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (Token.STRING != right.getType()) {
                // Only eval if they are the same type
                return n;
            }
            switch (op) {
                case Token.SHEQ:
                case Token.EQ:
                    result = left.getString().equals(right.getString());
                    break;
                case Token.SHNE:
                case Token.NE:
                    result = !left.getString().equals(right.getString());
                    break;
                default:
                    // we only handle == and != here
                    return n;
            }
            break;
        case Token.NUMBER:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (Token.NUMBER != right.getType()) {
                // Only eval if they are the same type
                return n;
            }
            Boolean compareResult = compareAsNumbers(op, left, right);
            if (compareResult != null) {
                result = compareResult;
            } else {
                return null;
            }
            break;
        case Token.NAME:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (rightLiteral) {
                boolean undefinedLeft = (left.getString().equals("undefined"));
                if (undefinedLeft) {
                    result = compareToUndefined(right, op);
                    break;
                }
            }
            if (Token.NAME != right.getType()) {
                // Only eval if they are the same type
                return n;
            }
            String ln = left.getString();
            String rn = right.getString();
            if (!ln.equals(rn)) {
                // Not the same value name.
                return n;
            }
            switch (op) {
                // If we knew the named value wouldn't be NaN, it would be nice
                // to handle EQ,NE,LE,GE,SHEQ, and SHNE.
                case Token.LT:
                case Token.GT:
                    result = false;
                    break;
                default:
                    // don't handle that op
                    return n;
            }
            break;
        default:
            // assert, this should cover all consts
            return n;
    }
    Node newNode = new Node(result ? Token.TRUE : Token.FALSE);
    n.replaceWith(newNode);
    reportCodeChange();
    return newNode;
}