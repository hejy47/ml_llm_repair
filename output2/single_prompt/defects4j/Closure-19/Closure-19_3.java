protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
    switch(node.getToken()) {
        case NAME:
            scope.inferSlotType(node.getString(), type);
            break;
        case GETPROP:
            String qualifiedName = node.getQualifiedName();
            Preconditions.checkNotNull(qualifiedName);
            JSType origType = node.getJSTypeBeforeOverride();
            origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
            scope.inferQualifiedSlot(node, qualifiedName, origType, type);
            break;
        // "this" references aren't currently modeled in the CFG.
        default:
            throw new IllegalArgumentException("Node cannot be refined. \n" + node.toStringTree());
    }
}