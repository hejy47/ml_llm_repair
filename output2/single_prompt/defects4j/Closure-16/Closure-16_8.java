## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree();
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    if (typeReference.isString()) {
        typeReference.setString(aliasName);
    } else {
        throw new IllegalStateException("Cannot apply alias to non-string type reference");
    }
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
            Node aliasedTypeNode = aliasedNode.cloneTree();
            aliasedTypeNode.setString(name.substring(endIndex));
            aliasUsages.add(new AliasedTypeNode(aliasedTypeNode, aliasedNode.getQualifiedName() + name.substring(endIndex)));
            return;
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}