## Fixed Function 1
protected void registerTypeVariablesOn(Type classType) {
    if (!(classType instanceof ParameterizedType)) {
        return;
    }
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    Type rawType = parameterizedType.getRawType();
    if (!(rawType instanceof Class)) {
        return;
    }
    TypeVariable<?>[] typeParameters = ((Class<?>) rawType).getTypeParameters();
    for (int i = 0; i < typeParameters.length && i < actualTypeArguments.length; i++) {
        TypeVariable<?> typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];
        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }
        // logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualTypeArgument + "(" + System.identityHashCode(typeParameter) + ")" + "' }");
    }
}