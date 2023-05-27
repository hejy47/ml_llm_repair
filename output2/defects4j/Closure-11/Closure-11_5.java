private void visitGetProp(NodeTraversal t, Node n, Node parent) {
    // obj.prop or obj.method()
    // Lots of types can appear on the left, a call to a void function can
    // never be on the left. getPropertyType will decide what is acceptable
    // and what isn't.
    Node property = n.getLastChild();
    Node objNode = n.getFirstChild();
    JSType childType = getJSType(objNode);
    if (childType.isDict()) {
        report(t, objNode, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
        return;
    } 
    if (n.getJSType() != null && parent.isAssign()) {
        return;
    } 
    if (validator.expectNotNullOrUndefined(t, n, childType, "No properties on this expression", getNativeType(OBJECT_TYPE))) {
        checkPropertyAccess(childType, property.getString(), t, n);
    }
    ensureTyped(t, n);
}