private void computeGenKill(Node n, BitSet gen, BitSet kill, boolean conditional) {
    switch(n.getType()) {
        case Token.SCRIPT:
        case Token.BLOCK:
        case Token.FUNCTION:
            return;
        case Token.WHILE:
        case Token.DO:
        case Token.IF:
            computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true); // always set conditional to true for if/while/do
            return;
        case Token.FOR:
            if (!NodeUtil.isForIn(n)) {
                computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true); // always set conditional to true for for loops
            } else {
                // for(x in y) {...}
                Node lhs = n.getFirstChild();
                Node rhs = lhs.getNext();
                if (NodeUtil.isVar(lhs)) {
                    // for(var x in y) {...}
                    lhs = lhs.getLastChild();
                }
                addToSetIfLocal(lhs, kill);
                addToSetIfLocal(rhs, gen); // fix assignment direction
                computeGenKill(rhs, gen, kill, true); // always set conditional to true for for-in loops
            }
            return;
        case Token.VAR:
            for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                if (c.hasChildren()) {
                    computeGenKill(c.getFirstChild(), gen, kill, true); // always set conditional to true for var declarations
                    addToSetIfLocal(c.getFirstChild(), gen); // fix missing addToSetIfLocal
                    if (!conditional) {
                        addToSetIfLocal(c.getFirstChild(), kill);
                    }
                }
            }
            return;
        case Token.AND:
        case Token.OR:
            computeGenKill(n.getFirstChild(), gen, kill, true); // always set conditional to true for boolean operators
            // May short circuit.
            computeGenKill(n.getLastChild(), gen, kill, true); // always set conditional to true for boolean operators
            return;
        case Token.HOOK:
            computeGenKill(n.getFirstChild(), gen, kill, true); // always set conditional to true for ternary operators
            // Assume both sides are conditional.
            computeGenKill(n.getFirstChild().getNext(), gen, kill, true); // always set conditional to true for ternary operators
            computeGenKill(n.getLastChild(), gen, kill, true); // always set conditional to true for ternary operators
            return;
        case Token.NAME:
            if (isArgumentsName(n)) {
                markAllParametersEscaped();
            } else {
                addToSetIfLocal(n, gen);
                if (!conditional) {
                    addToSetIfLocal(n, kill); // fix missing addToSetIfLocal
                }
            }
            return;
        default:
            if (NodeUtil.isAssignmentOp(n) && NodeUtil.isName(n.getFirstChild())) {
                Node lhs = n.getFirstChild();
                if (!conditional) {
                    addToSetIfLocal(lhs, kill);
                }
                if (!NodeUtil.isAssign(n)) {
                    // assignments such as a += 1 reads a.
                    addToSetIfLocal(lhs, gen);
                }
                computeGenKill(n.getLastChild(), gen, kill, true); // always set conditional to true for assignments
            } else {
                for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                    computeGenKill(c, gen, kill, true); // always set conditional to true for other node types
                }
            }
            return;
    }
}