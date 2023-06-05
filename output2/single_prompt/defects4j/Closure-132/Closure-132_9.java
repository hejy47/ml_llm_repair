private Node tryMinimizeIf(Node n) {
    Node parent = n.getParent();
    Node cond = n.getFirstChild();
    if (NodeUtil.isLiteralValue(cond, true)) {
        return n;
    }
    Node thenBranch = cond.getNext();
    Node elseBranch = thenBranch.getNext();

    if (elseBranch == null) {
        if (isFoldableExpressBlock(thenBranch)) {
            Node expr = getBlockExpression(thenBranch);
            if (!late && isPropertyAssignmentInExpression(expr)) {
                // Keep opportunities for CollapseProperties such as
                // a.longIdentifier || a.longIdentifier = ... -> var a = ...;
                // until CollapseProperties has been run.
                return n;
            }
            if (cond.isNot()) {
                // if(!x)bar(); -> x||bar();
                if (isLowerPrecedenceInExpression(cond, OR_PRECEDENCE) && isLowerPrecedenceInExpression(expr.getFirstChild(), OR_PRECEDENCE)) {
                    // It's not okay to add two sets of parentheses.
                    return n;
                }
                Node or = IR.or(cond.removeFirstChild(), expr.removeFirstChild()).srcref(n);
                Node newExpr = NodeUtil.newExpr(or);
                parent.replaceChild(n, newExpr);
                reportCodeChange();
                return newExpr;
            }
            // if(x)foo(); -> x&&foo();
            if (isLowerPrecedenceInExpression(cond, AND_PRECEDENCE) && isLowerPrecedenceInExpression(expr.getFirstChild(), AND_PRECEDENCE)) {
                // One additional set of parentheses is worth the change even if
                // there is no immediate code size win. However, two extra pair of
                // {}, we would have to think twice. (unless we know for sure the
                // we can further optimize its parent.
                return n;
            }
            n.removeChild(cond);
            Node and = IR.and(cond, expr.removeFirstChild()).srcref(n);
            Node newExpr = NodeUtil.newExpr(and);
            parent.replaceChild(n, newExpr);
            reportCodeChange();
            return newExpr;
        } else {
            // Try to combine two IF-ELSE
            if (NodeUtil.isStatementBlock(thenBranch) && thenBranch.hasOneChild()) {
                Node innerIf = thenBranch.getFirstChild();
                if (innerIf.isIf()) {
                    Node innerCond = innerIf.getFirstChild();
                    Node innerThenBranch = innerCond.getNext();
                    Node innerElseBranch = innerThenBranch.getNext();
                    if (innerElseBranch == null && !(isLowerPrecedenceInExpression(cond, AND_PRECEDENCE) && isLowerPrecedenceInExpression(innerCond, AND_PRECEDENCE))) {
                        n.detachChildren();
                        n.addChildToBack(IR.and(cond, innerCond.detachFromParent()).srcref(cond));
                        n.addChildrenToBack(innerThenBranch.detachFromParent());
                        reportCodeChange();
                        // Not worth trying to fold the current IF-ELSE into && because
                        // the inner IF-ELSE wasn't able to be folded into && anyways.
                        return n;
                    }
                }
            }
            return n;
        }
    }

    // Check if there are any repeated statements in the then or else branches
    tryRemoveRepeatedStatements(n);

    // if(!x)foo();else bar(); -> if(x)bar();else foo();
    // An additional set of curly braces isn't worth it.
    if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
        n.replaceChild(cond, cond.removeFirstChild());
        n.removeChild(thenBranch);
        n.addChildToBack(thenBranch);
        reportCodeChange();
        return n;
    }
    // if(x)return 1;else return 2; -> return x?1:2;
    if (isReturnExpressBlock(thenBranch) && isReturnExpressBlock(elseBranch)) {
        Node thenExpr = getBlockReturnExpression(thenBranch);
        Node elseExpr = getBlockReturnExpression(elseBranch);
        Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
        Node returnNode = IR.returnNode(hookNode).srcref(n);
        parent.replaceChild(n, returnNode);
        reportCodeChange();
        return returnNode;
    }
    boolean thenBranchIsExpressionBlock = isFoldableExpressBlock(thenBranch);
    boolean elseBranchIsExpressionBlock = isFoldableExpressBlock(elseBranch);
    if (thenBranchIsExpressionBlock && elseBranchIsExpressionBlock) {
        Node thenOp = getBlockExpression(thenBranch).getFirstChild();
        Node elseOp = getBlockExpression(elseBranch).getFirstChild();
        if (thenOp.getType() == elseOp.getType()) {
            // if(x)a=1;else a=2; -> a=x?1:2;
            if (NodeUtil.isAssignmentOp(thenOp)) {
                Node lhs = thenOp.getFirstChild();
                if (areNodesEqualForInlining(lhs, elseOp.getFirstChild()) && !mayEffectMutableState(lhs)) {
                    Node assignName = thenOp.removeFirstChild();
                    Node thenExpr = thenOp.removeFirstChild();
                    Node elseExpr = elseOp.getLastChild().detachFromParent();
                    Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
                    Node assign = new Node(thenOp.getType(), assignName, hookNode).srcref(thenOp);
                    Node expr = NodeUtil.newExpr(assign);
                    parent.replaceChild(n, expr);
                    reportCodeChange();
                    return expr;
                }
            }
        }
        // if(x)foo();else bar(); -> x?foo():bar()
        Node thenExpr = getBlockExpression(thenBranch).removeFirstChild();
        Node elseExpr = getBlockExpression(elseBranch).removeFirstChild();
        Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
        Node expr = NodeUtil.newExpr(hookNode);
        parent.replaceChild(n, expr);
        reportCodeChange();
        return expr;
    }
    boolean thenBranchIsVar = isVarBlock(thenBranch);
    boolean elseBranchIsVar = isVarBlock(elseBranch);
    // if(x)var y=1;else y=2  ->  var y=x?1:2
    if (thenBranchIsVar && elseBranchIsExpressionBlock && getBlockExpression(elseBranch).getFirstChild().isAssign()) {
        Node var = getBlockVar(thenBranch);
        Node elseAssign = getBlockExpression(elseBranch).getFirstChild();
        Node name1 = var.getFirstChild();
        Node maybeName2 = elseAssign.getFirstChild();
        if (name1.hasChildren() && maybeName2.isName() && name1.getString().equals(maybeName2.getString())) {
            Node thenExpr = name1.removeChildren();
            Node elseExpr = elseAssign.getLastChild().detachFromParent();
            Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
            var.detachFromParent();
            name1.addChildrenToBack(hookNode);
            parent.replaceChild(n, var);
            reportCodeChange();
            return var;
        }
        // if(x)y=1;else var y=2  ->  var y=x?1:2
    } else if (elseBranchIsVar && thenBranchIsExpressionBlock && getBlockExpression(thenBranch).getFirstChild().isAssign()) {
        Node var = getBlockVar(elseBranch);
        Node thenAssign = getBlockExpression(thenBranch).getFirstChild();
        Node maybeName1 = thenAssign.getFirstChild();
        Node name2 = var.getFirstChild();
        if (name2.hasChildren() && maybeName1.isName() && maybeName1.getString().equals(name2.getString())) {
            Node thenExpr = thenAssign.getLastChild().detachFromParent();
            Node elseExpr = name2.removeChildren();
            Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
            var.detachFromParent();
            name2.addChildrenToBack(hookNode);
            parent.replaceChild(n, var);
            reportCodeChange();
            return var;
        }
    }
    return n;
}