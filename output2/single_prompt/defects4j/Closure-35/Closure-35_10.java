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
                        // Fix - check if the property type is a function and infer a callable type instead
                        if (propType.isFunctionType()) {
                            typeToInfer = getNativeType(FUNCTION_TYPE);
                        } else {
                            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
                        }
                    }
                    objType.defineInferredProperty(prop, typeToInfer, null);
                }
            }
        }
    }
}