## Fixed Function 1
private void readTypeVariables() {
    for (Type bound : typeVariable.getBounds()) {
        registerTypeVariablesOn(bound);
    }
    Type actualTypeArg = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArg != null) {
        registerTypeVariablesOn(actualTypeArg);
    }
}