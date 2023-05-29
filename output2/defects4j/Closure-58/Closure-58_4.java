private void computeGenKill(Node n, BitSet gen, BitSet kill, boolean conditional) {
    switch(n.getType()) {
        case Token.SCRIPT:
        case Token.BLOCK:
        case Token.FUNCTION:
            return;
        case Token.WHILE:
        case Token.DO:
        case Token.IF:
            computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true);
            computeGenKill(n.getFirstChild(), gen, kill, conditional);
            return;
        case Token.FOR:
            if (!NodeUtil.isForIn(n)) {
                computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true);
                computeGenKill(NodeUtil.getForInIterator(n), gen, kill, false);
                computeGenKill(n.getLastChild(), gen, kill, conditional);
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
                computeGenKill(rhs, gen, kill, true);
                computeGenKill(n.getLastChild(), gen, kill, conditional);
            }
            return;
        case Token.VAR:
            for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                if (c.hasChildren()) {
                    computeGenKill(c.getFirstChild(), gen, kill, true);
                    if (!conditional) {
                        addToSetIfLocal(c.getFirstChild(), kill);
                    }
                }
            }
            return;
        case Token.AND:
        case Token.OR:
            computeGenKill(n.getFirstChild(), gen, kill, true);
            computeGenKill(n.getLastChild(), gen, kill, true);
            return;
        case Token.HOOK:
            computeGenKill(n.getFirstChild(), gen, kill, true);
            computeGenKill(n.getFirstChild().getNext(), gen, kill, true);
            computeGenKill(n.getLastChild(), gen, kill, true);
            return;
        case Token.NAME:
            if (isArgumentsName(n)) {
                markAllParametersEscaped();
            } else {
                addToSetIfLocal(n, gen);
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
                computeGenKill(lhs.getNext(), gen, kill, true);
            } else {
                for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                    computeGenKill(c, gen, kill, true);
                }
            }
            return;
    }
}