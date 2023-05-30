private void updateSimpleDeclaration(String alias, Name refName, Ref ref) {
    Node rvalue = ref.node.getNext();
    Node parent = ref.node.getParent();
    Node gramps = parent.getParent();
    Node greatGramps = gramps.getParent();
    Node greatGreatGramps = greatGramps.getParent();
    // Create the new alias node.
    Node nameNode = NodeUtil.newName(compiler.getCodingConvention(), alias, gramps.getFirstChild(), refName.fullName());
    NodeUtil.copyNameAnnotations(ref.node.getLastChild(), nameNode);
    if (gramps.getType() == Token.EXPR_RESULT) {
        // BEFORE: a.b.c = ...;
        //   exprstmt
        //     assign
        //       getprop
        //         getprop
        //           name a
        //           string b
        //         string c
        //       NODE
        // AFTER: var a$b$c = ...;
        //   var
        //     name a$b$c
        //       NODE
        // Remove the rvalue (NODE).
        parent.removeChild(rvalue);
        nameNode.addChildToFront(rvalue);
        Node varNode = new Node(Token.VAR, nameNode);
        greatGramps.replaceChild(gramps, varNode);
    } else {
        // This must be a complex assignment.
        Preconditions.checkNotNull(ref.getTwin());
        // BEFORE:
        // ... (x.y = 3);
        //
        // AFTER:
        // var x$y;
        // ... (x$y = 3);
        Node currentParent = gramps;
        for (;;) {
            currentParent = currentParent.getParent();
            if (currentParent == null) {
                // Could not find a SCRIPT or BLOCK parent node above gramps
                throw new IllegalStateException("Could not find a SCRIPT or BLOCK parent node for complex assignment");
            } else if (currentParent.getType() == Token.SCRIPT || currentParent.getType() == Token.BLOCK) {
                break;
            }
        }
        // Create a stub variable declaration right
        // before the current statement.
        Node stubVar = new Node(Token.VAR, nameNode.cloneTree()).copyInformationFrom(nameNode);
        currentParent.addChildBefore(stubVar, gramps);
        parent.replaceChild(ref.node, nameNode);
    }
    compiler.reportCodeChange();
}