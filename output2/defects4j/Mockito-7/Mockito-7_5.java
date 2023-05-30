## Fixed Function 1
private void readTypeVariables() {
    Type[] bounds = typeVariable.getBounds();
    for (Type type : bounds) {
        registerTypeVariablesOn(type);
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    registerTypeVariablesOn(actualTypeArgument);
} 

Note: The typeVariable used in this function needs to be initialized before calling this function.