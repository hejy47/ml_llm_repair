## Fixed Function 1
FunctionTypeBuilder inferFromOverriddenFunction(@Nullable FunctionType oldType, @Nullable Node paramsParent) {
    if (oldType == null) {
        return this;
    }
    returnType = oldType.getReturnType();
    returnTypeInferred = oldType.isReturnTypeInferred();
    if (paramsParent == null) {
        // Not a function literal.
        parametersNode = oldType.getParametersNode();
        if (parametersNode == null) {
            parametersNode = new FunctionParamBuilder(typeRegistry).build();
        }
    } else {
        // We're overriding with a function literal. Apply type information
        // to each parameter of the literal.
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(typeRegistry);
        Iterator<Node> oldParams = oldType.getParameters().iterator();
        boolean warnedAboutArgList = false;
        boolean oldParamsListHitOptArgs = false;
        Node currentParam = paramsParent.getFirstChild();
        while (currentParam != null && oldParams.hasNext()) {
            Node oldParam = oldParams.next();
            Node newParam = paramBuilder.newParameterFromNode(oldParam);
            oldParamsListHitOptArgs = oldParamsListHitOptArgs || oldParam.isVarArgs() || oldParam.isOptionalArg();
            // The subclass method might write its var_args as individual
            // arguments.
            if (currentParam.getNext() != null && newParam.isVarArgs()) {
                newParam.setVarArgs(false);
                newParam.setOptionalArg(true);
            }

            currentParam = currentParam.getNext();
        }

        while (oldParams.hasNext()) {
            Node oldParam = oldParams.next();
            warnedAboutArgList |= addParameter(paramBuilder, typeRegistry.getNativeType(UNKNOWN_TYPE), warnedAboutArgList, codingConvention.isOptionalParameter(oldParam), oldParamsListHitOptArgs || oldParam.isVarArgs() || oldParam.isOptionalArg());
        }
        // Clone any remaining params that aren't in the function literal.
        while (currentParam != null) {
            Node newParam = paramBuilder.newParameterFromNode(currentParam);
            warnedAboutArgList |= addParameter(paramBuilder, typeRegistry.getNativeType(UNKNOWN_TYPE), warnedAboutArgList, codingConvention.isOptionalParameter(currentParam), oldParamsListHitOptArgs || newParam.isVarArgs() || newParam.isOptionalArg());
            currentParam = currentParam.getNext();
        }

        parametersNode = paramBuilder.build();
    }
    return this;
}

## Fixed Function 2
FunctionTypeBuilder inferParameterTypes(@Nullable Node argsParent, @Nullable JSDocInfo info) {
    if (argsParent == null) {
        if (info == null) {
            return this;
        } else {
            return inferParameterTypes(info);
        }
    }

    List<JSType> oldParameterTypes = (parametersNode == null) ? Collections.<JSType>emptyList() : parametersNode.getJSTypes();
    List<String> oldParameterNames = (parametersNode == null) ? Collections.<String>emptyList() : parametersNode.getNames();
    FunctionParamBuilder builder = new FunctionParamBuilder(typeRegistry);
    boolean warnedAboutArgList = false;
    Set<String> allJsDocParams = (info == null) ? Sets.<String>newHashSet() : Sets.newHashSet(info.getParameterNames());
    int nArgs = argsParent.getChildCount();
    int nParams = oldParameterTypes.size();
    int i;
    for (i = 0; i < nArgs && i < nParams; i++) {
        Node arg = argsParent.getChildAtIndex(i);
        String paramName = oldParameterNames.get(i);
        JSType oldParameterType = oldParameterTypes.get(i);
        allJsDocParams.remove(paramName);
        JSType parameterType = null;
        boolean isOptional = isOptionalParameter(arg, info);
        boolean isVarArgs = isVarArgsParameter(arg, info);
        if (info != null && info.hasParameterType(paramName)) {
            parameterType = info.getParameterType(paramName).evaluate(scope, typeRegistry);
        } else {
            parameterType = oldParameterType.restrictByNotNullOrUndefined();
        }
        if (templateTypeName != null && parameterType.isTemplateType()) {
            if (foundTemplateType) {
                reportError(TEMPLATE_TYPE_DUPLICATED, fnName);
            }
            foundTemplateType = true;
        }
        warnedAboutArgList |= addParameter(builder, parameterType, warnedAboutArgList, isOptional, isVarArgs);
    }

    // Add remaining parameters
    for (; i < nParams; i++) {
        JSType oldParameterType = oldParameterTypes.get(i);
        boolean isOptional = oldParameterType.isOptionalArg();
        boolean isVarArgs = oldParameterType.isVarArgs();
        JSType parameterType = oldParameterType.restrictByNotNullOrUndefined();
        warnedAboutArgList |= addParameter(builder, parameterType, warnedAboutArgList, isOptional, isVarArgs);
    }

    // Add remaining args
    for (; i < nArgs; i++) {
        Node arg = argsParent.getChildAtIndex(i);
        boolean isOptional = isOptionalParameter(arg, info);
        boolean isVarArgs = isVarArgsParameter(arg, info);
        JSType parameterType = typeRegistry.getNativeType(UNKNOWN_TYPE);
        warnedAboutArgList |= addParameter(builder, parameterType, warnedAboutArgList, isOptional, isVarArgs);
    }

    if (templateTypeName != null && !foundTemplateType) {
        reportError(TEMPLATE_TYPE_EXPECTED, fnName);
    }
    for (String inexistentName : allJsDocParams) {
        reportWarning(INEXISTANT_PARAM, inexistentName, fnName);
    }

    parametersNode = builder.build();
    return this;
}