private void inlineNonConstants(Var v, ReferenceCollection referenceInfo, boolean maybeModifiedArguments) {
    int refCount = referenceInfo.references.size();
    Reference declaration = referenceInfo.references.get(0);
    Reference init = referenceInfo.getInitializingReference();
    int firstRefAfterInit = (declaration == init) ? 2 : 3;
    if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
        // if the variable is referenced more than once, we can only
        // inline it if it's immutable and never defined before referenced.
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

    } else if (!maybeModifiedArguments && !staleVars.contains(v) && referenceInfo.isWellDefined() && referenceInfo.isAssignedOnceInLifetime()) {
        // If this variable was not inlined normally, check if we can
        // inline an alias of it. (If the variable was inlined, then the
        // reference data is out of sync. We're better off just waiting for
        // the next pass.)
        // Inlining the variable based solely on well-defined and assigned
        // once is *NOT* correct. We relax the correctness requirement if
        // the variable is declared constant.
        List<Reference> refs = referenceInfo.references;
        for (int i = 1; i < refs.size(); i++) {
            Reference aliasReference = refs.get(i);
            Node nameNode = aliasReference.getNode();
            if (aliasCandidates.containsKey(nameNode)) {
                AliasCandidate candidate = aliasCandidates.get(nameNode);
                Reference aliasInit = candidate.refInfo.getInitializingReference();
                if (aliasInit == null || !aliasInit.isDeleted()) {
                    // Only inline if alias reference is not deleted and doesn't have a null initialization.
                    Node value = aliasReference.getAssignedValue();
                    Preconditions.checkNotNull(value);
                    inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
                    staleVars.add(candidate.alias);
                }
            }
        }
    }
}