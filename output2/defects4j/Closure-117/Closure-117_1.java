String getReadableJSTypeName(Node n, boolean dereference) {
    // The best type name is the actual type name.
    // If we're analyzing a GETPROP, the property may be inherited by the
    // prototype chain. So climb the prototype chain and find out where
    // the property was originally defined.
    if (n.isGetProp()) {
        ObjectType objectType = getJSType(n.getFirstChild()).dereference();
        if (objectType != null) {
            String propName = n.getLastChild().getString();
            if (objectType.getConstructor() != null && objectType.getConstructor().isInterface()) {
                objectType = FunctionType.getTopDefiningInterface(objectType, propName);
            } else {
                // classes
                while (objectType != null && !objectType.hasOwnProperty(propName)) {
                    objectType = objectType.getImplicitPrototype();
                }
            }
            // Don't show complex function names or anonymous types.
            // Instead, try to get a human-readable type name.
            if (objectType != null && (objectType.getConstructor() != null || objectType.isFunctionPrototypeType())) {
                return objectType.toString() + "." + propName;
            }
        }
    }
    JSType type = getJSType(n);
    if (dereference) {
        ObjectType dereferenced = type.dereference();
        if (dereferenced != null) {
            type = dereferenced;
        }
    }
    ObjectType objectType = type.toObjectType();
    if (type.isFunctionPrototypeType() || objectType != null && objectType.getConstructor() instanceof FunctionType) {
        // Return a more readable type name for function prototypes and function types.
        FunctionType constructor = (FunctionType) objectType.getConstructor();
        // Return qualified function name only if it is not anonymous.
        if (constructor != null && constructor.isNominalConstructor() && !constructor.getDisplayName().isEmpty()) {
            return constructor.getDisplayName();
        } else {
            return "function";
        }
    }
    String qualifiedName = n.getQualifiedName();
    if (qualifiedName != null) {
        return qualifiedName;
    } else {
        return type.toString();
    }
}