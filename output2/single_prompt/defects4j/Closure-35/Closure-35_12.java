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
                        typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
                    }
                    // fix: properties from both constraintObj and objType must be non null
                    if(constraintObj.getPropertyType(prop)!=null && objType.getPropertyType(prop)!=null)
                        objType.defineInferredProperty(prop, typeToInfer, null);
                }
            }
        }
    }
}