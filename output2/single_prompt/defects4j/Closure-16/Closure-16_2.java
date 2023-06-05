## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.cloneTree();
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setToken(Token.NAME, aliasName);
}

## Fixed Function 3
private void fixTypeNode(Node typeNode) {
    if (typeNode.isName()) {
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
            fixTypeNode(aliasedTypeNode);
            Node newName = new Node(Token.NAME, aliasedNode.getQualifiedName() + name.substring(endIndex));
            typeNode.replaceWith(newName);
            aliasedTypeNode.getLastChild().addChildToBack(newName);
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}