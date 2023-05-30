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
    options.setProcessClosurePrimitives(flags.processClosurePrimitives); // Fixed
    options.setProcessJqueryPrimitives(CompilationLevel.ADVANCED_OPTIMIZATIONS == level && flags.processJqueryPrimitives); // Fixed
    options.setAngularPass(flags.angularPass); // Fixed
    if (!flags.translationsFile.isEmpty()) {
        try {
            options.messageBundle = new XtbMessageBundle(new FileInputStream(flags.translationsFile), flags.translationsProject);
        } catch (IOException e) {
            throw new RuntimeException("Reading XTB file", e);
        }
    } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
        options.messageBundle = new EmptyMessageBundle();
    }
    return options;
}