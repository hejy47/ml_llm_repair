## Fixed Function 1
AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference.clone(); // Use .clone() to avoid mutating original object
    this.aliasName = aliasName;
}

## Fixed Function 2
@Override
public void applyAlias() {
    typeReference.setString(aliasName);
    typeReference.setType(Type.ALIAS); // Set type as alias
}

## Fixed Function 3
private void fixTypeNode(Node typeNode) {
    if (typeNode.getType() == Type.STRING) { // Use type checking instead of isString()
        String name = typeNode.getString();
        int endIndex = name.indexOf('.');
        if (endIndex == -1) {
            endIndex = name.length();
        }
        String baseName = name.substring(0, endIndex);
        Var aliasVar = aliases.get(baseName);
        if (aliasVar != null) {
            Node aliasedNode = aliasVar.getInitialValue();
            Node aliasedTypeNode = new AliasedTypeNode(aliasedNode, name.substring(endIndex)); // Create new AliasedTypeNode
            aliasUsages.add(aliasedTypeNode);
        }
    }
    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}