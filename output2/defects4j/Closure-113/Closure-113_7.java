private void processRequireCall(NodeTraversal t, Node n, Node parent) {
    Node left = n.getFirstChild();
    Node arg = left.getNext();
    if (verifyLastArgumentIsString(t, left, arg)) {
        String ns = arg.getString();
        ProvidedName provided = providedNames.get(ns);
        if (provided == null || !provided.isExplicitlyProvided()) {
            unrecognizedRequires.add(new UnrecognizedRequire(n, ns, t.getSourceName()));
        } else {
            JSModule providedModule = provided.explicitModule;
            // This must be non-null, because there was an explicit provide.
            Preconditions.checkNotNull(providedModule);
            JSModule module = t.getModule();
            if (moduleGraph != null && module != providedModule && !moduleGraph.dependsOn(module, providedModule)) {
                compiler.report(t.makeError(n, XMODULE_REQUIRE_ERROR, ns, providedModule.getName(), module.getName()));
            }
        }
        // Fixed: Only add to symbol table if provided name exists.
        if (provided != null) {
            maybeAddToSymbolTable(left);
            maybeAddStringNodeToSymbolTable(arg);
        }
        // Fixed: Only detach if provided name exists and is not explicitly provided.
        if (provided != null && !provided.isExplicitlyProvided()) {
            parent.detachFromParent();
            compiler.reportCodeChange();
        }
    }
}