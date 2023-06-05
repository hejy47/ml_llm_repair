## Fixed Function 1
@Override
JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
    boolean resolved = resolveViaRegistry(t, enclosing);
    if (!resolved) {
        resolveViaProperties(t, enclosing);
    }
    if (detectImplicitPrototypeCycle()) {
        handleTypeCycle(t);
    }
    super.resolveInternal(t, enclosing);
    if (isResolved()) {
        finishPropertyContinuations();
    }
    return registry.isLastGeneration() ? getReferencedType() : this;
} 

The fixed function simply changes the order of the `resolveViaRegistry()` and `resolveViaProperties()` calls so that the latter is only called when the former fails to resolve the type. This ensures that both mechanisms are used, but in the correct order.