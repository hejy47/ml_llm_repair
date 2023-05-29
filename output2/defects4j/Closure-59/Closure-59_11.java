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
        options.setCheckTypes(true);
    } else if (options.disables(DiagnosticGroups.CHECK_TYPES)) {
        options.setCheckTypes(false);
    } else if (!options.checkTypes()) {
        // If DiagnosticGroups did not override the plain checkTypes
        // option, and checkTypes is enabled, then turn off the
        // parser type warnings.
        options.setWarningLevel(DiagnosticGroups.forName("missingProperties"), CheckLevel.OFF);
    }
    if (options.checkGlobalThisLevel().isOn()) {
        options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, options.checkGlobalThisLevel());
    }
    if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
        options.setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR);
    }
    // Initialize the warnings guard.
    List<WarningsGuard> guards = Lists.newArrayList();
    guards.add(new SuppressDocWarningsGuard(getRegisteredGroups()));
    guards.add(options.getWarningsGuard());
    ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);
    // All passes must run the variable check. This synthesizes
    // variables later so that the compiler doesn't crash. It also
    // checks the externs file for validity. If you don't want to warn
    // about missing variable declarations, we shut that specific
    // error off.
    if (!options.checkSymbols() && !composedGuards.enables(DiagnosticGroups.CHECK_VARIABLES)) {
        composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
    }
    this.warningsGuard = composedGuards;
}