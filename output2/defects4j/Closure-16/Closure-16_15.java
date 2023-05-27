## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree();
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setString(aliasName);
    typeReference.removeChildren(); // remove children of the type reference node to prevent memory leaks
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
            Node clonedTypeNode = typeNode.cloneTree(); // clone the type node to prevent modifying the original tree
            aliasedNode.replaceWith(clonedTypeNode); // replace the alias node with the cloned type node
            aliasUsages.add(new AliasedTypeNode(typeNode, aliasedNode.getQualifiedName() + name.substring(endIndex)));
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}