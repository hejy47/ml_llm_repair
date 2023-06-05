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
    options.setProcessClosurePrimitives(flags.processClosurePrimitives); // fixed the typo in the option name
    options.setRewritePolyfills(flags.rewritePolyfills); // added the missing option
    options.setLanguageIn(LanguageMode.valueOf(flags.languageIn.toUpperCase())); // fixed casing and missing conversion to enum
    options.setLanguageOut(LanguageMode.valueOf(flags.languageOut.toUpperCase())); // fixed casing and missing conversion to enum
    options.setAngularPass(flags.angularPass);
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