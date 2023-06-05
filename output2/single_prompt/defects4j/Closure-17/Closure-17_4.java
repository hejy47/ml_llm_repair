## Fixed Function 1
private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
    if (info != null && info.hasType()) {
        // Return declared type from JSDoc annotation
        return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    } else if (rValue != null && rValue.isFunction() && shouldUseFunctionLiteralType(JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
        // Return declared type from function literal
        JSType functionType = JSType.toMaybeFunctionType(rValue.getJSType());
        if (functionType != null && functionType.isFunctionType()) {
            return functionType;
        }
    } else if (info != null) {
        if (info.hasEnumParameterType()) {
            if (rValue != null && rValue.isObjectLit()) {
                // Return JSType from object literal
                return rValue.getJSType();
            } else {
                // Create enum JSType from declaration nodes
                return createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
            }
        } else if (info.isConstructor() || info.isInterface()) {
            // Create constructor or interface JSType from declaration nodes
            return createFunctionTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
        } else {
            // Check if this is a constant with a known type
            if (info.isConstant() && rValue != null && rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
                return rValue.getJSType();
            }
        }
    }
    // If no type found, return declared type from JSDoc annotation
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}