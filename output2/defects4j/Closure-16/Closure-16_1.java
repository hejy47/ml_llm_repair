## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree(); // fix: create a new copy of the node to avoid aliasing
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setString(aliasName);
    typeReference.setCharn(-1); // fix: reset the char position to avoid unexpected behavior
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
            Node aliasedNode = aliasVar.getInitialValue().cloneTree(); // fix: create a new copy of the node to avoid aliasing
            aliasUsages.add(new AliasedTypeNode(aliasedNode, aliasedNode.getQualifiedName() + name.substring(endIndex)));
        } else {
            aliasUsages.add(new AliasedTypeNode(typeNode.cloneTree(), name)); // fix: create a new copy of the node if there are no aliases
        }
    } else {
        Node clonedNode = typeNode.cloneTree(); // fix: create a new copy of the node to avoid aliasing
        for (Node child = clonedNode.getFirstChild(); child != null; child = child.getNext()) {
            fixTypeNode(child);
        }
    }
}