private void computeGenKill(Node n, BitSet gen, BitSet kill, boolean conditional) {
    switch(n.getType()) {
        case Token.SCRIPT:
        case Token.BLOCK:
        case Token.FUNCTION:
            return;
        case Token.WHILE:
        case Token.DO:
        case Token.IF:
            computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true); //fix: set conditional to true for if statements
            return;
        case Token.FOR:
            if (!NodeUtil.isForIn(n)) {
                computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true); //fix: set conditional to true for for loops
            } else {
                // for(x in y) {...}
                Node lhs = n.getFirstChild();
                Node rhs = lhs.getNext();
                if (NodeUtil.isVar(lhs)) {
                    // for(var x in y) {...}
                    lhs = lhs.getLastChild();
                }
                addToSetIfLocal(lhs, kill);
                addToSetIfLocal(lhs, gen);
                computeGenKill(rhs, gen, kill, true); //fix: set conditional to true for for-in loops
            }
            return;
        case Token.VAR:
            for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                if (c.hasChildren()) {
                    computeGenKill(c.getFirstChild(), gen, kill, true); //fix: set conditional to true for var statements
                    if (!conditional) {
                        addToSetIfLocal(c.getFirstChild(), kill); //fix: add variable to kill set if not conditional
                    }
                }
            }
            return;
        case Token.AND:
        case Token.OR:
            computeGenKill(n.getFirstChild(), gen, kill, true); //fix: set conditional to true for logical operators
            computeGenKill(n.getLastChild(), gen, kill, true); //fix: set conditional to true for logical operators
            return;
        case Token.HOOK:
            computeGenKill(n.getFirstChild(), gen, kill, true); //fix: set conditional to true for ternary operator
            // Assume both sides are conditional.
            computeGenKill(n.getFirstChild().getNext(), gen, kill, true); //fix: set conditional to true for ternary operator
            computeGenKill(n.getLastChild(), gen, kill, true); //fix: set conditional to true for ternary operator
            return;
        case Token.NAME:
            if (isArgumentsName(n)) {
                markAllParametersEscaped();
            } else {
                addToSetIfLocal(n, gen);
                if (!conditional) {
                    addToSetIfLocal(n, kill); //fix: add variable to kill set if not conditional
                }
            }
            return;
        default:
            if (NodeUtil.isAssignmentOp(n) && NodeUtil.isName(n.getFirstChild())) {
                Node lhs = n.getFirstChild();
                addToSetIfLocal(lhs, gen);
                if (!conditional) {
                    addToSetIfLocal(lhs, kill); //fix: add variable to kill set if not conditional
                }
                if (!NodeUtil.isAssign(n)) {
                    // assignments such as a += 1 reads a.
                    computeGenKill(lhs.getNext(), gen, kill, true); //fix: set conditional to true for assignments
                }
            } else {
                for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                    computeGenKill(c, gen, kill, true); //fix: set conditional to true for other nodes
                }
            }
            return;
    }
}