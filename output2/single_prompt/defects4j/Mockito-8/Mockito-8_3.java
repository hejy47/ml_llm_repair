## Fixed Function 1
protected void registerTypeVariablesOn(Type classType) {
    if (!(classType instanceof ParameterizedType)) {
        return;
    }
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    Type rawType = parameterizedType.getRawType();
    if (rawType instanceof Class) {
        TypeVariable[] typeParameters = ((Class<?>) rawType).getTypeParameters();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            TypeVariable<?> typeParameter = typeParameters[i];
            Type actualTypeArgument = actualTypeArguments[i];
            if (actualTypeArgument instanceof WildcardType) {
                contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
            } else {
                contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
            }
        }
    }
}