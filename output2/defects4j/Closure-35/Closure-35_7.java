## Fixed Function 1
private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
    if (type == null || constraint == null) {
        return;
    }
    ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
    if (constraintObj != null && constraintObj.isRecordType()) {
        ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
        if (objType != null) {
            for (String prop : constraintObj.getOwnPropertyNames()) {
                JSType propType = constraintObj.getPropertyType(prop);
                if (!objType.isPropertyTypeDeclared(prop)) {
                    JSType typeToInfer = propType;
                    if (!objType.hasProperty(prop)) {
                        //use getLeastSupertypeOrNull instead of getLeastSupertype to handle case where propType is null
                        typeToInfer = getNativeType(VOID_TYPE).getLeastSupertypeOrNull(propType);
                    }
                    if(typeToInfer != null){
                        objType.defineInferredProperty(prop, typeToInfer, null);
                    }
                }
            }
        }
    }
}