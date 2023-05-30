@Override
protected CompilerOptions createOptions() {
    CompilerOptions options = new CompilerOptions();
    if (flags.processJqueryPrimitives) {
        options.setCodingConvention(new JqueryCodingConvention());
    } else {
        options.setCodingConvention(new ClosureCodingConvention());
    }
    options.setExtraAnnotationNames(flags.extraAnnotationName);
    CompilationLevel level = flags.compilationLevel;
    level.setOptionsForCompilationLevel(options);
    if (flags.debug) {
        level.setDebugOptionsForCompilationLevel(options);
    }
    if (flags.useTypesForOptimization) {
        level.setTypeBasedOptimizationOptions(options);
    }
    if (flags.generateExports) {
        options.setGenerateExports(flags.generateExports);
    } else {
        options.setGenerateExports(false); // Add this line to ensure that generateExports is explicitly set to false if not specified in flags
    }
    WarningLevel wLevel = flags.warningLevel;
    wLevel.setOptionsForWarningLevel(options);
    for (FormattingOption formattingOption : flags.formatting) {
        formattingOption.applyToOptions(options);
    }
    options.closurePass = flags.processClosurePrimitives;
    options.jqueryPass = CompilationLevel.ADVANCED_OPTIMIZATIONS == level && flags.processJqueryPrimitives;
    options.angularPass = flags.angularPass;
    if (!flags.translationsFile.isEmpty()) {
        try {
            options.messageBundle = new XtbMessageBundle(new FileInputStream(flags.translationsFile), flags.translationsProject);
        } catch (IOException e) {
            throw new RuntimeException("Reading XTB file", e);
        }
    } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
        // In SIMPLE or WHITESPACE mode, if the user hasn't specified a
        // translations file, they might reasonably try to write their own
        // implementation of goog.getMsg that makes the substitution at
        // run-time.
        //
        // In ADVANCED mode, goog.getMsg is going to be renamed anyway,
        // so we might as well inline it. But shut off the i18n warnings,
        // because the user didn't really ask for i18n.
        options.messageBundle = new EmptyMessageBundle();
    } else {
        options.messageBundle = null; // Add this line to ensure that messageBundle is explicitly set to null if not specified in flags
    }
    return options;
}