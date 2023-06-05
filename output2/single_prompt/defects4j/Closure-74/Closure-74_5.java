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
    switch(lhType) {
        case Token.VOID:
            if (!NodeUtil.isLiteralValue(left.getFirstChild(), false)) {
                return n;
            }
            if (!rightLiteral) {
                result = compareToUndefined(right, op);
            } else {
                result = compareToUndefined(left, op);
            }
            break;
        case Token.NULL:
        case Token.TRUE:
        case Token.FALSE:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
            } else if (rhType != Token.TRUE && rhType != Token.FALSE && rhType != Token.NULL) {
                return n;
            }
            else{
                result = compareLiteral(op, left, right);
            }
            break;
        case Token.THIS:
            if (right.getType() != Token.THIS) {
                return n;
            }
            switch(op) {
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
            if (!rightLiteral || Token.STRING != right.getType()) {
                return n;
            }
            switch(op) {
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
            } else if (Token.NUMBER != right.getType()) {
                // Only eval if they are the same type
                return n;
            }
            else{
                result = compareLiteral(op, left, right);
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
            switch(op) {
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

// Helper function to compare two literals and return the comparison result
private boolean compareLiteral(int op, Node left, Node right){
    switch(op) {
        case Token.SHEQ:
        case Token.EQ:
            return left.equals(right);
        case Token.SHNE:
        case Token.NE:
            return !left.equals(right);
        case Token.GE:
        case Token.LE:
        case Token.GT:
        case Token.LT:
            Boolean compareResult = compareAsNumbers(op, left, right);
            if (compareResult != null) {
                return compareResult;
            } else {
                return false;
            }
        default:
            // we only handle == and != here
            return false;
    }
}

// Helper function to compare a node with undefined
private boolean compareToUndefined(Node node, int op){
    switch(op) {
        case Token.SHEQ:
        case Token.EQ:
            return (node.getType() == Token.NULL) || (node.getType() == Token.NAME && node.getString().equals("undefined")) || (Token.VOID == node.getType() && NodeUtil.isLiteralValue(node.getFirstChild(), false));
        case Token.SHNE:
        case Token.NE:
            return (node.getType() != Token.NULL) && !(node.getType() == Token.NAME && node.getString().equals("undefined")) && !(Token.VOID == node.getType() && NodeUtil.isLiteralValue(node.getFirstChild(), false));
        default:
            return false;
    }
}