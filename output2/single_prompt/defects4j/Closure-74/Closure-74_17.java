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
        case Token.STRING:
        case Token.NUMBER:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (!NodeUtil.isLiteralValue(right, false)) {
                // Only eval if they are the same type
                return n;
            }
            switch (op) {
                case Token.SHEQ:
                case Token.EQ:
                    result = left.isEquivalentTo(right);
                    break;
                case Token.SHNE:
                case Token.NE:
                    result = !left.isEquivalentTo(right);
                    break;
                default:
                    // we only handle == and != here
                    return n;
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
    n.getParent().replaceChild(n, newNode);
    reportCodeChange();
    return newNode;
}