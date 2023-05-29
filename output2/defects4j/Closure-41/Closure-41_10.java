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
        for (Node currentParam = paramsParent.getFirstChild(); currentParam != null; currentParam = currentParam.getNext()) {
            if (oldParams.hasNext()) {
                Node oldParam = oldParams.next();
                Node newParam = paramBuilder.newParameterFromNode(oldParam);
                oldParamsListHitOptArgs = oldParamsListHitOptArgs || oldParam.isVarArgs() || oldParam.isOptionalArg();
                // The subclass method might write its var_args as individual
                // arguments.
                if (currentParam.getNext() != null && newParam.isVarArgs()) {
                    newParam.setVarArgs(false);
                    newParam.setOptionalArg(true);
                }
                parametersNode.addChildToBack(newParam);
            } else {
                warnedAboutArgList |= addParameter(paramBuilder, typeRegistry.getNativeType(UNKNOWN_TYPE), warnedAboutArgList, codingConvention.isOptionalParameter(currentParam) || oldParamsListHitOptArgs, codingConvention.isVarArgsParameter(currentParam));
                Node newParam = paramBuilder.buildSingleParameterNode();
                if (newParam != null) {
                    parametersNode.addChildToBack(newParam);
                }
            }
        }
        // Clone any remaining params that aren't in the function literal.
        for (int numParamsToAdd = oldType.getParametersCount() - parametersNode.getChildCount(); numParamsToAdd > 0; numParamsToAdd--) {
            paramBuilder.buildSingleParameterNode(true);
            Node newParam = paramBuilder.buildSingleParameterNode();
            if (newParam != null) {
                parametersNode.addChildToBack(newParam);
            }
        }
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
    // arguments
    Node oldParameterType = null;
    if (parametersNode != null) {
        oldParameterType = parametersNode.getFirstChild();
    }
    FunctionParamBuilder builder = new FunctionParamBuilder(typeRegistry);
    boolean warnedAboutArgList = false;
    Set<String> allJsDocParams = (info == null) ? Sets.<String>newHashSet() : Sets.newHashSet(info.getParameterNames());
    boolean foundTemplateType = false;
    boolean isVarArgs = false;
    for (Node arg : argsParent.children()) {
        String argumentName = arg.getString();
        allJsDocParams.remove(argumentName);
        // type from JSDocInfo
        JSType parameterType = null;
        boolean isOptionalParam = isOptionalParameter(arg, info);
        isVarArgs = isVarArgsParameter(arg, info);
        if (info != null && info.hasParameterType(argumentName)) {
            parameterType = info.getParameterType(argumentName).evaluate(scope, typeRegistry);
        } else if (oldParameterType != null && oldParameterType.getJSType() != null) {
            parameterType = oldParameterType.getJSType();
            isOptionalParam = oldParameterType.isOptionalArg();
            isVarArgs = oldParameterType.isVarArgs();
        } else {
            parameterType = typeRegistry.getNativeType(UNKNOWN_TYPE);
        }
        if (templateTypeName != null && parameterType.restrictByNotNullOrUndefined().isTemplateType()) {
            if (foundTemplateType) {
                reportError(TEMPLATE_TYPE_DUPLICATED, fnName);
            }
            foundTemplateType = true;
        }
        warnedAboutArgList |= addParameter(builder, parameterType, warnedAboutArgList, isOptionalParam, isVarArgs);
        oldParameterType = oldParameterType != null ? oldParameterType.getNext() : null;
    }
    // Copy over any old parameters that aren't in the param list.
    if (templateTypeName != null && !foundTemplateType) {
        reportError(TEMPLATE_TYPE_EXPECTED, fnName);
    }
    for (String inexistentName : allJsDocParams) {
        reportWarning(INEXISTANT_PARAM, inexistentName, fnName);
    }
    while (oldParameterType != null) {
        builder.copyParameter(oldParameterType, true);
        oldParameterType = oldParameterType.getNext();
    }
    parametersNode = builder.build();
    return this;
}