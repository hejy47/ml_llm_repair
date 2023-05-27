## Fixed Function 1
@Override
public JSType caseObjectType(ObjectType type) {
    if (value.equals("function")) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
        // Functions are restricted to "Function", subtypes are left
        // Only filter out subtypes of "Function"
    }
    return matchesExpectation("object") ? type : null;
}