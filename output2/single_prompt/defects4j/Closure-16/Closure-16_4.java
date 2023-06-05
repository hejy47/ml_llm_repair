## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree();
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setString(aliasName);
    typeReference.setBoolean(true); // added to mark as aliased
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
            Node aliasedTypeNode = new AliasedTypeNode(aliasedNode.cloneTree(), name.substring(endIndex));
            aliasedTypeNode.setLineno(typeNode.getLineno()); // inherits line no of original type node
            typeNode.getParent().replaceChild(typeNode, aliasedTypeNode);
        }
    } else {
        for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
            fixTypeNode(child);
        }
    }
}