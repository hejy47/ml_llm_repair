public void initOptions(CompilerOptions options) {
    this.options = options;
    if (errorManager == null) {
        if (outStream == null) {
            setErrorManager(new LoggerErrorManager(createMessageFormatter(), logger));
        } else {
            PrintStreamErrorManager printer = new PrintStreamErrorManager(createMessageFormatter(), outStream);
            printer.setSummaryDetailLevel(options.summaryDetailLevel);
            setErrorManager(printer);
        }
    }
    // DiagnosticGroups override the plain checkTypes option.
    if (options.enables(DiagnosticGroups.CHECK_TYPES)) {
        options.checkTypes = true;
    } else if (options.disables(DiagnosticGroups.CHECK_TYPES)) {
        options.checkTypes = false;
    } else if (!options.checkTypes) {
        // If DiagnosticGroups did not override the plain checkTypes
        // option, and checkTypes is enabled, then turn off the
        // parser type warnings.
        options.setWarningLevel(
            DiagnosticGroups.forType(DiagnosticGroups.VISIBILITY),
            CheckLevel.OFF);
    }
    if (options.checkGlobalThisLevel.isOn()) {
        options.setWarningLevel(
            DiagnosticGroups.forName("globalThis"),
            options.checkGlobalThisLevel);
    }
    if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
        options.setWarningLevel(
            DiagnosticGroups.forName("es5Strict"),
            CheckLevel.ERROR);
    }
    // Initialize the warnings guard.
    List<WarningsGuard> guards = Lists.newArrayList();
    guards.add(new SuppressDocWarningsGuard(getDiagnosticGroups().getRegisteredGroups()));
    guards.add(options.getWarningsGuard());
    ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);
    // All passes must run the variable check. This synthesizes
    // variables later so that the compiler doesn't crash. It also
    // checks the externs file for validity. If you don't want to warn
    // about missing variable declarations, we shut that specific
    // error off.
    if (!options.checkSymbols && !composedGuards.enables(DiagnosticGroups.VISIBILITY)) {
        composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.VISIBILITY, CheckLevel.OFF));
    }
    this.warningsGuard = composedGuards;
}