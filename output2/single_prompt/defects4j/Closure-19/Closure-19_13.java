## Fixed Function 1
protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
    switch(node.getToken()) { // Use getToken() instead of getType() for Node types
        case NAME: // Use Token.NAME instead of hardcoding integer value
            scope.inferSlotType(node.getString(), type);
            break;
        case GETPROP: // Use Token.GETPROP instead of hardcoding integer value
            String qualifiedName = node.getQualifiedName();
            Preconditions.checkNotNull(qualifiedName);
            JSType origType = node.getJSType();
            origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
            scope.inferQualifiedSlot(node, qualifiedName, origType, type);
            break;
        // "this" references aren't currently modeled in the CFG.
        default:
            throw new IllegalArgumentException("Node cannot be refined. \n" + node.toStringTree());
    }
}