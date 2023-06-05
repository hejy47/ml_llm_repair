## Fixed Function 1
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
        options.setWarningLevel(DiagnosticGroups.forType(RhinoErrorReporter.TYPE_PARSE_ERROR), CheckLevel.OFF);
    }
    if (options.checkGlobalThisLevel.isOn()) {
        options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, options.checkGlobalThisLevel);
    }
    if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
        options.setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR);
    }
    // Initialize the warnings guard.
    List<WarningsGuard> guards = Lists.newArrayList();
    guards.add(new SuppressDocWarningsGuard(getDiagnosticGroups().getRegisteredGroups()));
    guards.add(options.getWarningsGuard());
    ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);
    // All checks on symbols should be done so that JSCompiler does not crash.
    boolean checkSymbols = true;
    if (options.checkSymbols) { // overridden
        checkSymbols = true;
    } else {
        // Check variables only if there is no warning guard that disables CHECK_VARIABLES.
        checkSymbols = !composedGuards.disables(DiagnosticGroups.CHECK_VARIABLES);
    }
    if (!checkSymbols) {
        composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
    }
    this.warningsGuard = composedGuards;
}