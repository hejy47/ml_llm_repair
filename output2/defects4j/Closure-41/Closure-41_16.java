## Fixed Function 1
FunctionTypeBuilder inferFromOverriddenFunction(@Nullable FunctionType oldType, @Nullable Node paramsParent) {
    FunctionTypeBuilder builder = new FunctionTypeBuilder(typeRegistry);
    if (oldType == null) {
        return builder;
    }
    builder.returnType(oldType.getReturnType());
    builder.returnTypeInferred(oldType.isReturnTypeInferred());
    if (paramsParent == null) {
        // Not a function literal.
        builder.parametersNode(oldType.getParametersNode());
        if (builder.parametersNode() == null) {
            builder.parametersNode(new FunctionParamBuilder(typeRegistry).build());
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
            } else {
                warnedAboutArgList |= builder.addParameter(typeRegistry.getNativeType(UNKNOWN_TYPE), warnedAboutArgList, codingConvention.isOptionalParameter(currentParam) || oldParamsListHitOptArgs, codingConvention.isVarArgsParameter(currentParam));
            }
        }
        // Clone any remaining params that aren't in the function literal.
        builder.parametersNode(paramBuilder.build());
    }
    return builder;
}

## Fixed Function 2
FunctionTypeBuilder inferParameterTypes(@Nullable Node argsParent, @Nullable JSDocInfo info) {
    FunctionTypeBuilder builder = new FunctionTypeBuilder(typeRegistry);
    if (argsParent == null) {
        if (info == null) {
            return builder;
        } else {
            return builder.inferParameterTypes(info);
        }
    }
    // arguments
    Node oldParameterType = null;
    if (parametersNode != null) {
        oldParameterType = parametersNode.getFirstChild();
    }
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(typeRegistry);
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
        warnedAboutArgList |= builder.addParameter(parameterType, warnedAboutArgList, isOptionalParam, isVarArgs);
        if (oldParameterType != null) {
            oldParameterType = oldParameterType.getNext();
        }
    }
    // Copy over any old parameters that aren't in the param list.
    if (templateTypeName != null && !foundTemplateType) {
        reportError(TEMPLATE_TYPE_EXPECTED, fnName);
    }
    for (String inexistentName : allJsDocParams) {
        reportWarning(INEXISTANT_PARAM, inexistentName, fnName);
    }
    builder.parametersNode(paramBuilder.build());
    return builder;
}