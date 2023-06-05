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
        options.setCheckTypes(true); // Fix: Use setCheckTypes instead of directly accessing the field
    } else if (options.disables(DiagnosticGroups.CHECK_TYPES)) {
        options.setCheckTypes(false); // Fix: Use setCheckTypes instead of directly accessing the field
    } else if (!options.checkTypes()) { // Fix: Use checkTypes() instead of accessing the field directly
        // If DiagnosticGroups did not override the plain checkTypes
        // option, and checkTypes is enabled, then turn off the
        // parser type warnings.
        options.setWarningLevel(DiagnosticGroups.CHECK_TYPES, CheckLevel.OFF); // Fix: Use DiagnosticGroups.CHECK_TYPES instead of accessing the type directly
    }
    if (options.checkGlobalThisLevel().isOn()) { // Fix: Use checkGlobalThisLevel() instead of accessing the field directly
        options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, options.checkGlobalThisLevel()); // Fix: Use checkGlobalThisLevel() instead of accessing the field directly
    }
    if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
        options.setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR); // Fix: Use DiagnosticGroups.ES5_STRICT instead of accessing the type directly
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
    if (!options.checkSymbols() && !composedGuards.enables(DiagnosticGroups.CHECK_VARIABLES)) { // Fix: Use checkSymbols() instead of accessing the field directly
        composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF)); // Fix: Use DiagnosticGroups.CHECK_VARIABLES instead of accessing the type directly
    }
    this.warningsGuard = composedGuards;
}