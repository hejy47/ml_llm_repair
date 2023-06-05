## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree(); // clone the tree to avoid aliasing
    this.aliasName = aliasName;
}


## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setString(aliasName);
    typeReference.removeChildren(); // remove any children to avoid aliasing
}


## Fixed Function 3
private void fixTypeNode(Node typeNode) {
    if (typeNode.isString()) {
        String name = typeNode.getString();
        int endIndex = name.indexOf('.');
        if (endIndex == -1) {
            endIndex = name.length();
        }
        String baseName = name.substring(0, endIndex);
        Var aliasVar = aliases.get(baseName);
        if (aliasVar != null) {
            Node aliasedNode = aliasVar.getInitialValue();
            Node aliasedClone = aliasedNode.cloneTree(); // clone the aliased node to avoid aliasing
            String aliasedName = aliasedClone.getQualifiedName() + name.substring(endIndex);
            aliasUsages.add(new AliasedTypeNode(aliasedClone, aliasedName));
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child.cloneTree()); // clone the child to avoid aliasing
    }
}