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
        options.setWarningLevel(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.ERROR);
    } else if (options.disables(DiagnosticGroups.CHECK_TYPES)) {
        options.setWarningLevel(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF);
    } else if (!options.checkTypes) {
        // If DiagnosticGroups did not override the plain checkTypes
        // option, and checkTypes is enabled, then turn off the
        // parser type warnings.
        options.setWarningLevel(DiagnosticGroup.forType(RhinoErrorReporter.TYPE_PARSE_ERROR), CheckLevel.OFF);
        if (!options.checkSymbols) {
            options.setWarningLevel(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF);
        }
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
    this.warningsGuard = composedGuards;
}