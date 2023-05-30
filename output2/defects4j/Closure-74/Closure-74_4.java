@SuppressWarnings("fallthrough")
private Node tryFoldComparison(Node n, Node left, Node right) {
    if (!NodeUtil.isLiteralValue(left, false) || !NodeUtil.isLiteralValue(right, false)) {
        // We only handle non-literal operands for LT and GT.
        if (n.getType() != Token.GT && n.getType() != Token.LT) {
            return n;
        }
    }
    int op = n.getType();
    boolean result = false;
    boolean rightLiteral = NodeUtil.isLiteralValue(right, false);
    boolean undefinedRight = ((Token.NAME == right.getType() && right.getString().equals("undefined")) || (Token.VOID == right.getType() && NodeUtil.isLiteralValue(right.getFirstChild(), false)));
    int lhType = left.getType();
    int rhType = right.getType();
    switch (lhType) {
        case Token.VOID:
            // Fall through
        case Token.NULL:
            // Fall through
        case Token.TRUE:
            // Fall through
        case Token.FALSE:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (rightLiteral && rhType == lhType) {
                // Compare left and right nodes as values.
                result = left.getBoolean() == right.getBoolean();
            } else if (rhType == Token.NULL || rhType == Token.TRUE || rhType == Token.FALSE || rhType == Token.VOID) {
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
                        // Handle mixed boolean and numeric compares like:
                        // alert(true > true, true > false, false > true, false > false);
                        //
                        // Handle null/undefined like:
                        // alert(null > null, null >= null, null < null, null <= null);
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
            } else {
                return n;
            }
            break;
        case Token.NAME:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (rightLiteral && (rhType == Token.NUMBER || rhType == Token.STRING)) {
                switch (op) {
                    case Token.SHEQ:
                    case Token.EQ:
                        result = left.getBoolean() == right.getBoolean();
                        break;
                    case Token.SHNE:
                    case Token.NE:
                        result = left.getBoolean() != right.getBoolean();
                        break;
                    case Token.GE:
                    case Token.LE:
                    case Token.GT:
                    case Token.LT:
                        // Handle boolean and numeric compares with numbers stored
                        // as named values like: alert(true > 1, true < 2);
                        Boolean compareResult = compareAsNumbers(op, left, right);
                        if (compareResult != null) {
                            result = compareResult;
                        } else {
                            return n;
                        }
                        break;
                    default:
                        // Only EQ and NE handled below
                        return n;
                }
            } else if (right.getType() == Token.THIS && op == Token.SHEQ) {
                result = true;
            } else {
                return n;
            }
            break;
        case Token.STRING:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (rightLiteral && rhType == Token.STRING) {
                switch (op) {
                    case Token.SHEQ:
                    case Token.EQ:
                        result = left.getString().equals(right.getString());
                        break;
                    case Token.SHNE:
                    case Token.NE:
                        result = !left.getString().equals(right.getString());
                        break;
                    case Token.GE:
                    case Token.GT:
                    case Token.LE:
                    case Token.LT:
                        // Handle numeric compares with strings stored as literals
                        // or named values.
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
            } else {
                return n;
            }
            break;
        case Token.NUMBER:
            if (undefinedRight) {
                result = compareToUndefined(left, op);
                break;
            }
            if (rightLiteral && (rhType == Token.NUMBER)) {
                Boolean compareResult = compareAsNumbers(op, left, right);
                if (compareResult != null) {
                    result = compareResult;
                } else {
                    return n;
                }
            } else if (right.getType() == Token.NAME && right.getString().equals("NaN")) {
                switch (op) {
                    case Token.SHEQ:
                    case Token.EQ:
                        result = false;
                        break;
                    case Token.SHNE:
                    case Token.NE:
                        result = true;
                        break;
                    default:
                        return n;
                }
            } else {
                return n;
            }
            break;
        default:
            // assert, this shouldn't happen
            return n;
    }
    Node newNode = new Node(result ? Token.TRUE : Token.FALSE);
    n.getParent().replaceChild(n, newNode);
    reportCodeChange();
    return newNode;
}