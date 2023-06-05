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
            JSType propType = constraintObj.getPropertyType(prop);
            if (!isPropertyTypeDeclared(prop)) {
                JSType typeToInfer = propType;
                if (!hasProperty(prop)) {
                    // If the object doesn't have the property, define it with the least supertype
                    // of the property's type and the void type.
                    typeToInfer = JSType.join(getNativeType(JSTypeNative.VOID_TYPE), propType);
                }
                defineInferredProperty(prop, typeToInfer, null);
            }
        }
    }
}