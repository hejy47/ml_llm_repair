## Fixed Function 1
@Override
public void exitScope(NodeTraversal t) {
    if (t.getScopeDepth() > 2) {
        findNamespaceShadows(t);
    }
    if (t.getScopeDepth() == 2) {
        renameNamespaceShadows(t);
        aliases.clear();
        forbiddenLocals.clear();
        transformation = null;
        hasNamespaceShadows = false;
    }
}

## Fixed Function 2
private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        Node parent = n.getParent();
        boolean isVar = parent.isVar();
        boolean isFunctionDecl = NodeUtil.isFunctionDeclaration(parent);
        if (isVar && n.getFirstChild() != null && n.getFirstChild().isQualifiedName()) {
            recordAlias(v);
        } else if (v.isBleedingFunction()) {
            // Bleeding functions already get a BAD_PARAMETERS error, so just
            // do nothing.
        } else if (parent.getType() == Token.LP) {
            // Parameters of the scope function also get a BAD_PARAMETERS
            // error.
        } else if (isVar || isFunctionDecl) {
            boolean isHoisted = NodeUtil.isHoistedFunctionDeclaration(parent);
            Node grandparent = parent.getParent();
            Node value = v.getInitialValue() != null ? v.getInitialValue() : null;
            Node varNode = null;
            String name = n.getString();
            int nameCount = scopedAliasNames.count(name);
            scopedAliasNames.add(name);
            String globalName = "$jscomp.scope." + name + (nameCount == 0 ? "" : ("$" + nameCount));
            compiler.ensureLibraryInjected("base");

            if (isFunctionDecl) {
                // Replace "function NAME() { ... }" with "var NAME;".
                Node existingName = v.getNameNode();
                // We can't keep the local name on the function expression,
                // because IE is buggy and will leak the name into the global
                // scope. This is covered in more detail here:
                // http://wiki.ecmascript.org/lib/exe/fetch.php?id=resources:resources&cache=cache&media=resources:jscriptdeviationsfromes3.pdf
                //
                // This will only cause problems if this is a hoisted, recursive
                // function, and the programmer is using the hoisting.
                Node newName = IR.name("").useSourceInfoFrom(existingName);
                value.replaceChild(existingName, newName);
                varNode = IR.var(existingName).useSourceInfoFrom(existingName);
                grandparent.replaceChild(parent, varNode);
            } else {
                if (value != null) {
                    // If this is a VAR, we can just detach the expression and
                    // the tree will still be valid.
                    value.detachFromParent();
                }
                varNode = parent;
            }

            if (value != null || v.getJSDocInfo() != null) {
                Node newDecl = NodeUtil.newQualifiedNameNodeDeclaration(compiler.getCodingConvention(), globalName, value, v.getJSDocInfo()).useSourceInfoIfMissingFromForTree(n);
                NodeUtil.setDebugInformation(newDecl.getFirstChild().getFirstChild(), n, name);
                if (isHoisted) {
                    grandparent.addChildToFront(newDecl);
                } else {
                    grandparent.addChildBefore(newDecl, varNode);
                }
            }

            v.getNameNode().addChildToFront(NodeUtil.newQualifiedNameNode(compiler.getCodingConvention(), globalName, n, name));
            recordAlias(v);
        } else {
            // Do not other kinds of local symbols, like catch params.
            report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
    }
}

## Fixed Function 3
@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    if (isCallToScopeMethod(n)) {
        validateScopeCall(t, n, n.getParent());
    }
    if (t.getScopeDepth() < 2) {
        return;
    }
    int type = n.getType();
    Var aliasVar = null;
    if (type == Token.NAME) {
        String name = n.getString();
        Var lexicalVar = t.getScope().getVar(n.getString());
        if (lexicalVar != null && aliases.containsKey(name) && lexicalVar == aliases.get(name)) {
            aliasVar = lexicalVar;
        }
    }
    if (t.getScopeDepth() == 2) {
        if (aliasVar != null && NodeUtil.isLValue(n)) {
            if (aliasVar.getNode() == n) {
                aliasDefinitionsInOrder.add(n);
                return;
            } else {
                report(t, n, GOOG_SCOPE_ALIAS_REDEFINED, n.getString());
            }
        }
        if (type == Token.RETURN) {
            report(t, n, GOOG_SCOPE_USES_RETURN);
        } else if (type == Token.THIS) {
            report(t, n, GOOG_SCOPE_REFERENCES_THIS);
        } else if (type == Token.THROW) {
            report(t, n, GOOG_SCOPE_USES_THROW);
        }
    }
    if (t.getScopeDepth() >= 2) {
        if (aliasVar != null) {
            aliasUsages.add(new AliasedNode(aliasVar, n));
        }
        JSDocInfo info = n.getJSDocInfo();
        if (info != null) {
            for (Node node : info.getTypeNodes()) {
                fixTypeNode(node);
            }
        }
        // Check if the goog.scope() call is an immediate child of the SCRIPT
        // node. Depth > 2 refers to calls not at the root.
        if (t.getScopeDepth() > 2 && parent != null && parent.getType() == Token.SCRIPT) {
            report(t, n, GOOG_SCOPE_NOT_AT_ROOT);
        }
    }
}