## Fixed Function 1
void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info, Node n, Node parent, Node rhsValue, TypedScope scope) {
    Node ownerNode = n.getFirstChild();
    String ownerName = ownerNode.getQualifiedName();
    String qName = n.getQualifiedName();
    String propName = n.getLastChild().getString();
    Preconditions.checkArgument(qName != null && ownerName != null);
    // Precedence of type information on GETPROPs:
    // 1) @type annnotation / @enum annotation
    // 2) ASSIGN to FUNCTION literal
    // 3) @param/@return annotation (with no function literal)
    // 4) ASSIGN to something marked @const
    // 5) ASSIGN to anything else
    //
    // 1, 3, and 4 are declarations, 5 is inferred, and 2 is a declaration iff
    // the function has jsdoc or has not been declared before.
    //
    // FUNCTION literals are special because TypedScopeCreator is very smart
    // about getting as much type information as possible for them.
    // Determining type for #1 + #2 + #3 + #4
    JSType valueType = getDeclaredType(t.getSourceName(), info, n, rhsValue);
    if (valueType == null && rhsValue != null) {
        // Determining type for #5
        valueType = rhsValue.getJSType();
    }
    // Function prototypes are special.
    // It's a common JS idiom to do:
    // F.prototype = { ... };
    // So if F does not have an explicitly declared super type,
    // allow F.prototype to be redefined arbitrarily.
    if ("prototype".equals(propName)) {
        Var qVar = scope.getVar(qName);
        if (qVar != null) {
            // If the programmer has declared that F inherits from Super,
            // and they assign F.prototype to an object literal,
            // then they are responsible for making sure that the object literal's
            // implicit prototype is set up appropriately. We just obey
            // the @extends tag.
            if (!qVar.isTypeInferred()) {
                // If the programmer has declared that F inherits from Super,
                // and they assign F.prototype to some arbitrary expression,
                // there's not much we can do. We just ignore the expression,
                // and hope they've annotated their code in a way to tell us
                // what props are going to be on that prototype.
                return;
            }
            if (qVar.getScope() == scope) {
                scope.undeclare(qVar);
            }
        }
    }
    if (valueType == null) {
        if (parent.getType() == Token.EXPR_RESULT) {
            stubDeclarations.add(new StubDeclaration(n, t.getInput() != null && t.getInput().isExtern(), ownerName));
        }
        return;
    }
    boolean inferred = true;
    if (info != null) {
        // Determining declaration for #1 + #3 + #4
        inferred = !(info.hasType() || info.hasEnumParameterType() || (info.isConstant() && valueType != null && !valueType.isUnknownType()) || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
    }
    if (inferred) {
        // Determining declaration for #2
        inferred = !(rhsValue != null && rhsValue.getType() == Token.FUNCTION && (info != null || !scope.isDeclared(qName, false)));
    }
    if (!inferred) {
        ObjectType ownerType = getObjectSlot(ownerName);
        if (ownerType != null) {
            // Only declare this as an official property if it has not been
            // declared yet.
            boolean isExtern = t.getInput() != null && t.getInput().isExtern();
            if ((!ownerType.hasOwnProperty(propName) || ownerType.isPropertyTypeInferred(propName)) && ((isExtern && !ownerType.isNativeObjectType()) || !ownerType.isInstanceType())) {
                // If the property is undeclared or inferred, declare it now.
                ownerType.defineDeclaredProperty(propName, valueType, n);
            }
        }
        // If the property is already declared, the error will be
        // caught when we try to declare it in the current scope.
        defineSlot(n, parent, valueType, inferred);
    } else if (rhsValue != null && rhsValue.getType() == Token.TRUE) {
        // We declare these for delegate proxy method properties.
        FunctionType ownerType = JSType.toMaybeFunctionType(getObjectSlot(ownerName));
        if (ownerType != null) {
            JSType ownerTypeOfThis = ownerType.getTypeOfThis();
            String delegateName = codingConvention.getDelegateSuperclassName();
            JSType delegateType = delegateName == null ? null : typeRegistry.getType(delegateName);
            if (delegateType != null && ownerTypeOfThis.isSubtype(delegateType)) {
                defineSlot(n, parent, getNativeType(BOOLEAN_TYPE), true);
            }
        }
    }
}

## Fixed Function 2
public void setPrototypeBasedOn(ObjectType baseType, TypeRegistry registry, String referenceName) {
    // This is a bit weird. We need to successfully handle these
    // two cases:
    // Foo.prototype = new Bar();
    // and
    // Foo.prototype = {baz: 3};
    // In the first case, we do not want new properties to get
    // added to Bar. In the second case, we do want new properties
    // to get added to the type of the anonymous object.
    //
    // We handle this by breaking it into two cases:
    //
    // In the first case, we create a new PrototypeObjectType and set
    // its implicit prototype to the type being assigned. This ensures
    // that Bar will not get any properties of Foo.prototype, but properties
    // later assigned to Bar will get inherited properly.
    //
    // In the second case, we just use the anonymous object as the prototype.
    if (baseType.hasReferenceName()
            || baseType.isUnknownType()
            || isNativeObjectType()
            || baseType.isFunctionPrototypeType()
            || !(baseType instanceof PrototypeObjectType)) {
        baseType = new PrototypeObjectType(registry, referenceName + ".prototype", baseType);
    }
    setPrototype((PrototypeObjectType) baseType);
}

## Fixed Function 3
public boolean setPrototype(PrototypeObjectType prototype) {
    if (prototype == null) {
        return false;
    }
    // getInstanceType fails if the function is not a constructor
    if (isConstructor() && prototype == getInstanceType()) {
        return false;
    }
    boolean replacedPrototype = prototype != null;
    this.prototype = prototype;
    this.prototypeSlot = new SimpleSlot("prototype", prototype, true);
    this.prototype.setOwnerFunction(this);
    // Disassociating the old prototype makes this easier to debug--
    // we don't have to worry about two prototypes running around.
    if (isConstructor() || isInterface()) {
        FunctionType superClass = getSuperClassConstructor();
        if (superClass != null) {
            superClass.addSubType(this);
        }
        if (isInterface()) {
            for (ObjectType interfaceType : getExtendedInterfaces()) {
                if (interfaceType.getConstructor() != null) {
                    interfaceType.getConstructor().addSubType(this);
                }
            }
        }
    }
    if (replacedPrototype) {
        clearCachedValues();
    }
    return true;
}