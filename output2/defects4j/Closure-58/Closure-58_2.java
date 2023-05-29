private void computeGenKill(Node n, BitSet gen, BitSet kill, boolean conditional) {
    switch(n.getType()) {
        case Token.SCRIPT:
        case Token.BLOCK:
        case Token.FUNCTION:
            return;
        case Token.WHILE:
        case Token.DO:
        case Token.IF:
            computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true); // Set conditionals as true
            computeGenKill(n.getFirstChild(), gen, kill, conditional); // Check the body of the loop/condition
            return;
        case Token.FOR:
            if (!NodeUtil.isForIn(n)) {
                computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, true); // Set conditionals as true
                computeGenKill(n.getSecondChild(), gen, kill, true); // Check for loop initializer
                computeGenKill(n.getLastChild(), gen, kill, true); // Check for loop increment
                computeGenKill(n.getFirstChild(), gen, kill, conditional); // Check the body of the loop
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
                computeGenKill(rhs, gen, kill, true); // Evaluate y as true
                computeGenKill(n.getFirstChild(), gen, kill, true); // Check the body of the loop
            }
            return;
        case Token.VAR:
            for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                if (c.hasChildren()) {
                    computeGenKill(c.getFirstChild(), gen, kill, true); // Set conditionals as true
                    addToSetIfLocal(c, kill);
                    addToSetIfLocal(c, gen);
                }
            }
            return;
        case Token.AND:
        case Token.OR:
            computeGenKill(n.getFirstChild(), gen, kill, true); // Check the first clause as true
            computeGenKill(n.getLastChild(), gen, kill, true); // Check the second clause as true
            return;
        case Token.HOOK:
            computeGenKill(n.getFirstChild(), gen, kill, true); // Check the first clause as true
            computeGenKill(n.getFirstChild().getNext(), gen, kill, true); // Check the second clause as true
            computeGenKill(n.getLastChild(), gen, kill, true); // Check the third clause as true
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
                addToSetIfLocal(lhs, gen);
                computeGenKill(lhs.getNext(), gen, kill, true); // Set conditionals as true
            } else {
                for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
                    computeGenKill(c, gen, kill, true); // Set conditionals as true
                }
            }
            return;
    }
}