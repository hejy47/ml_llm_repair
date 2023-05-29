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
                        // Check if the property type of the constraint is a callable type
                        if (propType.isFunctionType()) {
                            // Infer a function type for the object property
                            typeToInfer = objType.getRegistry().getNativeType(
                                JSTypeNative.FUNCTION_INSTANCE_TYPE).getLeastSupertype(propType);
                        } else {
                            // Otherwise, infer a regular property type
                            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
                        }
                    }
                    objType.defineInferredProperty(prop, typeToInfer, null);
                }
            }
        }
    }
}