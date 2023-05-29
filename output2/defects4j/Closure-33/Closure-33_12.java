## Fixed Function 1
@Override
public void matchConstraint(ObjectType constraintObj) {
    // We only want to match contraints on anonymous types.
    // Handle the case where the constraint object is a record type.
    //
    // param constraintObj {{prop: (number|undefined)}}
    // function f(constraintObj) {}
    // f({});
    //
    // We want to modify the object literal to match the constraint, by
    // taking each property on the record and trying to match
    // properties on this object.
    if (constraintObj.isRecordType()) {
        for (String prop : constraintObj.getOwnPropertyNames()) {
            JSType propType = constraintObj.getOwnPropertyType(prop);
            if (!isPropertyTypeDeclared(prop)) {
                JSType typeToInfer = propType;
                if (!hasProperty(prop)) {
                    typeToInfer = JSType.join(getNativeType(JSTypeNative.VOID_TYPE), propType);
                }
                defineInferredProperty(prop, typeToInfer, null);
            }
        }
    }
}