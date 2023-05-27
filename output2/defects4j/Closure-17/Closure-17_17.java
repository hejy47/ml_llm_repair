## Fixed Function 1
private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
    if (info != null && info.hasType()) {
        return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    } else if (rValue != null && rValue.isFunction() && shouldUseFunctionLiteralType(JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
        return rValue.getJSType();
    } else if (info != null) {
        if (info.hasEnumParameterType()) {
            if (rValue != null) {
                return rValue.getJSType();
            } else {
                return createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
            }
        } else if (info.isConstructor() || info.isInterface()) {
            return createFunctionTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
        } else {
            // Check if this is constant, and if it has a known type.
            if (info.isConstant()) {
                if (rValue != null) {
                    if (rValue.isOr()) {
                        // Check for a very specific JS idiom:
                        // var x = x || TYPE;
                        // This is used by Closure's base namespace for esoteric
                        // reasons.
                        Node firstClause = rValue.getFirstChild();
                        Node secondClause = firstClause.getNext();
                        boolean namesMatch = firstClause.isName() && lValue.isName() && firstClause.getString().equals(lValue.getString());
                        if (namesMatch && secondClause.getJSType() != null && !secondClause.getJSType().isUnknownType()) {
                            return secondClause.getJSType();
                        }
                    } else if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
                        // If rValue has a type-cast or its type was already computed during scope creation, we use the type in the type-cast.
                        return rValue.getJSType();
                    }
                }
            }
        }
    }
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}