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
    }
    WarningLevel wLevel = flags.warningLevel;
    wLevel.setOptionsForWarningLevel(options);
    for (FormattingOption formattingOption : flags.formatting) {
        formattingOption.applyToOptions(options);
    }
    options.setClosurePassEnabled(flags.processClosurePrimitives); // fix: setClosurePassEnabled instead of closurePass
    options.setJqueryPassEnabled(CompilationLevel.ADVANCED_OPTIMIZATIONS == level && flags.processJqueryPrimitives);
    options.setAngularPassEnabled(flags.angularPass);
    if (!flags.translationsFile.isEmpty()) {
        try {
            options.setMessageBundle(new XtbMessageBundle(new FileInputStream(flags.translationsFile), flags.translationsProject));
        } catch (IOException e) {
            throw new RuntimeException("Reading XTB file", e);
        }
    } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
        options.setMessageBundle(new EmptyMessageBundle());
        options.setWarningLevel(DiagnosticGroups.I18N, CheckLevel.OFF); // fix: turn off i18n warnings
    }
    return options;
}