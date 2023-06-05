## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree();
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setString(aliasName);
    typeReference.removeChildren(); // remove any children since they were cloned into typeReference
}

## Fixed Function 3
private void fixTypeNode(Node typeNode) {
    if (typeNode.isQualifiedName()) { // check if node is a qualified name
        String name = typeNode.getQualifiedName();
        int endIndex = name.indexOf('.');
        if (endIndex == -1) {
            endIndex = name.length();
        }
        String baseName = name.substring(0, endIndex);
        Var aliasVar = aliases.get(baseName);
        if (aliasVar != null) {
            Node aliasedNode = aliasVar.getInitialValue();
            // create a new qualified name node with aliasedNode's qualified name and the remaining name
            QualifiedNameNode qualifiedNameNode = new QualifiedNameNode(
                aliasedNode.getQualifiedName() + name.substring(endIndex));
            aliasUsages.add(new AliasedTypeNode(qualifiedNameNode, qualifiedNameNode.getQualifiedName()));
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}