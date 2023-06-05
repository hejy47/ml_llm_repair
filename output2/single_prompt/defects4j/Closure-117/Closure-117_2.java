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
                // check if the property is defined on the object
                if (!objectType.hasOwnProperty(propName) && objectType.getImplicitPrototype() != null) {
                    objectType = objectType.getImplicitPrototype();
                }
                // check if the property is defined on the constructor
                if (objectType.getConstructor() != null && !objectType.getConstructor().hasOwnProperty(propName)) {
                    objectType = getSuperTypeConstructor(objectType.getConstructor());
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
    if (type.isFunctionPrototypeType() || (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
        return type.toString();
    }
    String qualifiedName = n.getQualifiedName();
    if (qualifiedName != null) {
        return qualifiedName;
    } else if (type.isFunctionType()) {
        // Don't show complex function names.
        return "function";
    } else {
        return type.toString();
    }
}

// helper function to get the constructor of a super class
private ObjectType getSuperTypeConstructor(ObjectType constructor) {
    ObjectType superClass = constructor.getImplicitPrototype();
    while (superClass != null) {
        if (superClass.hasOwnProperty("constructor")) {
            JSType superConstructorType = superClass.getPropertyType("constructor");
            if (superConstructorType != null && superConstructorType.isFunctionType() && superConstructorType.toObjectType() != null) {
                return superConstructorType.toObjectType();
            }
        }
        superClass = superClass.getImplicitPrototype();
    }
    return null;
}