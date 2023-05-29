private boolean canInline(Reference declaration, Reference initialization, Reference reference) {
    if (!isValidDeclaration(declaration) || !isValidInitialization(initialization) || !isValidReference(reference)) {
        return false;
    }
    // If the value is read more than once, skip it.
    // VAR declarations and EXPR_RESULT don't need the value, but other
    // ASSIGN expressions parents should.
    if (declaration != initialization && !initialization.getGrandparent().isExprResult() && !hasReadOtherThanReference(initialization, reference)) {
        return false;
    }
    // Be very conservative and do no cross control structures or
    // scope boundaries
    if (!sameBasicBlock(declaration, initialization, reference)) {
        return false;
    }
    // Do not inline into a call node. This would change
    // the context in which it was being called. For example,
    //   var a = b.c;
    //   a();
    // should not be inlined, because it calls a in the context of b
    // rather than the context of the window.
    //   var a = b.c;
    //   f(a)
    // is ok.
    Node value = initialization.getAssignedValue();
    Preconditions.checkState(value != null);
    if (value.isGetProp() && reference.getParent().isCall() && reference.getParent().getFirstChild() == reference.getNode()) {
        return false;
    }
    if (value.isFunction()) {
        Node callNode = reference.getParent();
        if (reference.getParent().isCall()) {
            CodingConvention convention = compiler.getCodingConvention();
            // Bug 2388531: Don't inline subclass definitions into class defining
            // calls as this confused class removing logic.
            SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
            if (relationship != null) {
                return false;
            }
            // issue 668: Don't inline singleton getter methods
            // calls as this confused class removing logic.
        }
    }
    return canMoveAggressively(value) || canMoveModerately(initialization, reference);
}

private boolean sameBasicBlock(Reference declaration, Reference initialization, Reference reference) {
    return declaration.getBasicBlock() == initialization.getBasicBlock() && initialization.getBasicBlock() == reference.getBasicBlock();
}

private boolean hasReadOtherThanReference(Reference initialization, Reference reference) {
    return initialization.references != 1 || !initialization.getNode().getLastChild().isEquivalentTo(reference.getNode());
}