## Fixed Function 1
private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariables(type);
    }
    registerTypeVariables(getActualTypeArgumentFor(typeVariable));
} 

Note: The fixed function assumes that the "registerTypeVariablesOn" method was originally intended to be "registerTypeVariables".