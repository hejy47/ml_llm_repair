## Fixed Function 1
@Override
public void enterScope(NodeTraversal t) {
    Node declarationRoot = t.getScopeRoot();
    Renamer renamer;
    if (nameStack.isEmpty()) {
        // If the contextual renamer is being used the starting context can not
        // be a function.
        Preconditions.checkState(declarationRoot.getType() != Token.FUNCTION || !(rootRenamer instanceof ContextualRenamer));
        Preconditions.checkState(t.inGlobalScope());
        renamer = rootRenamer;
    } else {
        renamer = nameStack.peek().forChildScope();
    }
    if (declarationRoot.getType() == Token.FUNCTION) {
        Node functionBody = declarationRoot.getLastChild();
        findDeclaredNames(functionBody, null, renamer);
        for (Node c = declarationRoot.getFirstChild().getNext().getFirstChild(); c != null; c = c.getNext()) {
            String name = c.getString();
            renamer.addDeclaredName(name);
        }
    } else if (declarationRoot.getType() != Token.FUNCTION) {
        // Add the block declarations
        findDeclaredNames(declarationRoot, null, renamer);
    }
    nameStack.push(renamer);
}

## Fixed Function 2
@Override
public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    switch(n.getType()) {
        case Token.FUNCTION:
            {
                // Add recursive function name, if needed.
                // NOTE: "enterScope" is called after we need to pick up this name.
                Renamer renamer = nameStack.peek().forChildScope();
                // If needed, add the function recursive name.
                String name = n.getFirstChild().getString();
                if (name != null && !name.isEmpty() && parent != null && NodeUtil.isFunctionExpression(n)) {
                    renamer.addDeclaredName(name);
                }
                // Add the function parameters
                Node functionArgs = n.getSecondChild();
                while (functionArgs != null) {
                    if (functionArgs.getType() == Token.NAME) {
                        renamer.addDeclaredName(functionArgs.getString());
                    }
                    functionArgs = functionArgs.getNext();
                }
                // Add the function body declarations
                nameStack.push(renamer);
            }
            break;
        case Token.CATCH:
            {
                Renamer renamer = nameStack.peek().forChildScope();
                String name = n.getFirstChild().getString();
                renamer.addDeclaredName(name);
                nameStack.push(renamer);
            }
            break;
    }
    return true;
}

## Fixed Function 3
@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    switch(n.getType()) {
        case Token.NAME:
            String newName = getReplacementName(n.getString());
            if (newName != null) {
                Renamer renamer = nameStack.peek();
                if (renamer.stripConstIfReplaced()) {
                    // TODO(johnlenz): Do we need to do anything about the javadoc?
                    n.removeProp(Node.IS_CONSTANT_NAME);
                }
                n.setString(newName);
                t.getCompiler().reportCodeChange();
            }
            break;
        case Token.FUNCTION:
            // Remove the function body scope
            // Remove function recursive name (if any).
            nameStack.pop().stripCurrentScope();
            break;
        // Note: The parameters and function body variables live in the
        // same scope, we introduce the scope when in the "shouldTraverse"
        // visit of LP, but remove it when when we exit the function above.
        case Token.CATCH:
            // Remove catch except name from the stack of names.
            nameStack.pop();
            break;
    }
}