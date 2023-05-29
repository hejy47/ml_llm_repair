## Fixed Function 1
@Override
public void matchConstraint(ObjectType constraintObj) {
    // We only want to match constraints on anonymous types.
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
            JSType propType = constraintObj.getPropertyType(prop);
            if (!isPropertyTypeDeclared(prop)) {
                JSType typeToInfer = propType;
                if (!hasProperty(prop)) {
                    // Infer a type if the property is not already declared.
                    // Here, we use the VOID type which is a subtype of all types.
                    // This ensures that the inferred type is always compatible with
                    // the declared type.
                    typeToInfer = getNativeType(JSTypeNative.VOID_TYPE).getLeastSupertype(propType);
                }
                // Declare the inferred property.
                defineInferredProperty(prop, typeToInfer, null);
            }
        }
    }
}