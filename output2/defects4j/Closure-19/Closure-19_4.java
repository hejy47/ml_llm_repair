## Fixed Function 1
protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
    switch(node.getType()) {
        case Token.NAME:
            scope.inferSlotType(node.getString(), type);
            break;
        case Token.GETPROP:
            String qualifiedName = node.getQualifiedName();
            if(qualifiedName != null) { // Fix: Check for null before using variable
                JSType origType = node.getJSType();
                origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
                scope.inferQualifiedSlot(node, qualifiedName, origType, type);
            }
            else {
                // Fix: Throw more specific exception with helpful message
                throw new IllegalArgumentException("Node GETPROP has null qualified name: \n" + node.toStringTree());
            }
            break;
        // "this" references aren't currently modeled in the CFG.
        default:
            throw new IllegalArgumentException("Node cannot be refined. \n" + node.toStringTree());
    }
}