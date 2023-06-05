private boolean canInline(Reference declaration, Reference initialization, Reference reference) {
    if (!isValidDeclaration(declaration) || !isValidInitialization(initialization) || !isValidReference(reference)) {
        return false;
    }
    // If the value is read more than once, skip it.
    // VAR declarations and EXPR_RESULT don't need the value, but other
    // ASSIGN expressions parents do.
    if (declaration != initialization && !initialization.getGrandparent().isExprResult()) {
        return false;
    }
    // Be very conservative and do not cross control structures or
    // scope boundaries
    if (!declaration.getBasicBlock().equals(initialization.getBasicBlock()) || !declaration.getBasicBlock().equals(reference.getBasicBlock())) {
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
    Preconditions.checkNotNull(value);
    if (value.isGetProp() && reference.getParent().isCall() && reference.getParent().getFirstChild() == reference) {
        return false;
    }
    if (value.isFunction()) {
        Node callNode = reference.getParent();
        if (callNode.isCall()) {
            CodingConvention convention = compiler.getCodingConvention();
            // Bug 2388531: Do not inline subclass definitions into class defining
            // calls as this confuses class removing logic.
            SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
            if (relationship != null) {
                return false;
            }
            // issue 668: Do not inline singleton getter methods
            // calls as this confuses class removing logic.
        }
    }
    return canMoveAggressively(value) || canMoveModerately(initialization, reference);
}