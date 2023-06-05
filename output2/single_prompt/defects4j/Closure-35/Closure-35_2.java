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
                        // Check if the propertyType is a function type, assign unknown return type if it is
                        if (propType.isFunctionType()) {
                            // The unknown return type should be declared as a "var"
                            typeToInfer = getNativeType(UNKNOWN_TYPE).withTypeVariable("var");
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