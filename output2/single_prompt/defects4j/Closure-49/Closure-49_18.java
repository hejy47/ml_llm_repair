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
        for (Node c = declarationRoot.getFirstChild(); c != null; c = c.getNext()) {
            if (NodeUtil.isFunctionBlock(c)) {
                for (Node param : NodeUtil.getFunctionParameters(c)) {
                    String paramName = param.getString();
                    renamer.addDeclaredName(paramName);
                }
                findDeclaredNames(NodeUtil.getFunctionBody(c), null, renamer);
            } else if (NodeUtil.isFunctionDeclaration(c)) {
                String name = c.getFirstChild().getString();
                renamer.addDeclaredName(name);
            }
        }
    } else {
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
                if (name != null && !name.isEmpty() && parent != null && !NodeUtil.isFunctionDeclaration(n)) {
                    renamer.addDeclaredName(name);
                }
                // Add the function parameters
                for (Node param : NodeUtil.getFunctionParameters(n)) {
                    String paramName = param.getString();
                    renamer.addDeclaredName(paramName);
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
            nameStack.pop();
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