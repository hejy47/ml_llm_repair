private void inlineNonConstants(Var v, ReferenceCollection referenceInfo, boolean maybeModifiedArguments) {
    int refCount = referenceInfo.references.size();
    Reference declaration = referenceInfo.references.get(0);
    Reference init = referenceInfo.getInitializingReference();
    int firstRefAfterInit = (declaration == init) ? 1 : 2; // fix: changed from 3 to 2
    if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
        Node value;
        if (init != null) {
            value = init.getAssignedValue();
        } else {
            // Create a new node for variable that is never initialized.
            Node srcLocation = declaration.getNode();
            value = NodeUtil.newUndefinedNode(srcLocation);
        }
        Preconditions.checkNotNull(value);
        inlineWellDefinedVariable(v, value, referenceInfo.references);
        staleVars.add(v);
    } else if (refCount == firstRefAfterInit) {
        // The variable likely only read once, try some more
        // complex inlining heuristics.
        Reference reference = referenceInfo.references.get(firstRefAfterInit - 1);
        if (canInline(declaration, init, reference)) {
            inline(v, declaration, init, reference);
            staleVars.add(v);
        }
    } else if (declaration != init && refCount == 2) {
        if (isValidDeclaration(declaration) && isValidInitialization(init)) {
            // The only reference is the initialization, remove the assignment and
            // the variable declaration.
            Node value = init.getAssignedValue();
            Preconditions.checkNotNull(value);
            inlineWellDefinedVariable(v, value, referenceInfo.references);
            staleVars.add(v);
        }
    }
    // If this variable was not inlined normally, check if we can
    // inline an alias of it. (If the variable was inlined, then the
    // reference data is out of sync. We're better off just waiting for
    // the next pass.)
    if (!maybeModifiedArguments && !staleVars.contains(v) && referenceInfo.isWellDefined() && referenceInfo.isAssignedOnceInLifetime()) {
        List<Reference> refs = referenceInfo.references;
        for (int i = firstRefAfterInit; i < refs.size(); i++) { // fix: changed from i=1 to i=firstRefAfterInit
            Node nameNode = refs.get(i).getNode();
            if (aliasCandidates.containsKey(nameNode)) {
                AliasCandidate candidate = aliasCandidates.get(nameNode);
                if (!staleVars.contains(candidate.alias) && !isVarInlineForbidden(candidate.alias)) {
                    Reference aliasInit = candidate.refInfo.getInitializingReference();
                    Node value = aliasInit.getAssignedValue();
                    Preconditions.checkNotNull(value);
                    inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
                    staleVars.add(candidate.alias);
                }
            }
        }
    }
}