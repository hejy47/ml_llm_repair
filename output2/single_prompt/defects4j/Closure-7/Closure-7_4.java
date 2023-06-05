## Fixed Function 1
@Override
public JSType caseObjectType(ObjectType type) {
    if (value.equals("function")) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        return (resultEqualsValue && ctorType != null && ctorType.isSubtype(type)) ? ctorType : null;
        // Objects are restricted to "Function", subtypes are left
        // Only filter out subtypes of "function"
    }
    return matchesExpectation("object") ? type : null;
}