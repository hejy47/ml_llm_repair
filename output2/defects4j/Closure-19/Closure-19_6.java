protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
    switch(node.getToken()) { // Use getToken instead of getType to handle GETPROP nodes correctly
        case NAME:
            scope.inferSlotType(node.getString(), type);
            break;
        case GETPROP:
            String qualifiedName = node.getQualifiedName();
            Preconditions.checkNotNull(qualifiedName);
            JSType origType = node.getJSType();
            origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
            scope.inferQualifiedSlot(node, qualifiedName, origType, type);
            break;
        default:
            throw new IllegalArgumentException("Node cannot be refined. \n" + node.toStringTree());
    }
}