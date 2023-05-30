private Node tryFoldShift(Node n, Node left, Node right) {
    if (left.getType() == Token.NUMBER && right.getType() == Token.NUMBER) {
        double result;
        double lval = left.getDouble();
        double rval = right.getDouble();
        // check ranges
        if (!(lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE)) {
            error(BITWISE_OPERAND_OUT_OF_RANGE, left);
            return n;
        }
        // Check if the shift amount is outside [0,32)
        if (!(rval >= 0 && rval < 32)) {
            error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
            return n;
        }
        // Convert the numbers to longs since Java requires this for unsigned shift right
        long lvalLong = (long) lval;
        long rvalLong = (long) rval;
        if (rvalLong != rval) {
            error(FRACTIONAL_BITWISE_OPERAND, right);
            return n;
        }
        switch(n.getType()) {
            case Token.LSH:
                result = lvalLong << rvalLong;
                break;
            case Token.RSH:
                result = lvalLong >> rvalLong;
                break;
            case Token.URSH:
                result = lvalLong >>> rvalLong;
                break;
            default:
                throw new AssertionError("Unknown shift operator: " + Node.tokenToName(n.getType()));
        }
        Node newNumber = Node.newNumber((double)result);
        n.getParent().replaceChild(n, newNumber);
        reportCodeChange();
        return newNumber;
    }
    return n;
}