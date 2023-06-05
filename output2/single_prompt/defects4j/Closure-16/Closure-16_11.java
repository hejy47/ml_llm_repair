## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = new Node(Token.NAME);
    this.typeReference.setString(typeReference.getString());
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setType(Token.NAME);
    typeReference.setString(aliasName);
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
            Node aliasedType = new Node(Token.NAME);
            aliasedType.setString(aliasedNode.getQualifiedName() + name.substring(endIndex));
            aliasUsages.add(new AliasedTypeNode(aliasedType, aliasedType.getString()));
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}