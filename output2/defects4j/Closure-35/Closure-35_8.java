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
                        // check if propType is a function type
                        if (propType.isFunctionType()) {
                            // if so, use the VOID_RETURN_TYPE as the inferred type to handle optional/missing parameters
                            typeToInfer = getNativeType(VOID_RETURN_TYPE);
                        } else {
                            // if not, use the propType as the inferred type
                            typeToInfer = propType;
                        }
                    }
                    objType.defineInferredProperty(prop, typeToInfer, null);
                }
            }
        }
    }
}