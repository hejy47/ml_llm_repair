private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
    if (type == null || constraint == null) {
        return;
    }
    ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
    if (constraintObj != null && constraintObj.isRecordType()) {
        ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
        if (objType != null) {
            for (String prop : constraintObj.getOwnPropertyNames()) {
                if (!objType.isPropertyTypeDeclared(prop)) {
                    JSType propType = constraintObj.getPropertyType(prop);
                    JSType typeToInfer = propType;
                    if (!objType.hasProperty(prop)) {
                        JSType voidType = getNativeType(VOID_TYPE);
                        typeToInfer = voidType == null ? propType : voidType.getLeastSupertype(propType);
                    }
                    objType.defineInferredProperty(prop, typeToInfer, null);
                }
            }
        }
    }
}