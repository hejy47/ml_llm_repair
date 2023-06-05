## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    this.isHoisted = NodeUtil.isFunctionDeclaration(root) && NodeUtil.isNameDeclaration(root);
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    List<Reference> refs = getAllAssignmentsInLifetime();
    return refs != null && refs.size() == 1 && !NodeUtil.isInLoop(refs.get(0).getAncestor(2));
}