## Fixed Function 1
@Override
protected CompilerOptions createOptions() {
    CompilerOptions options = new CompilerOptions();
    options.setCodingConvention(new ClosureCodingConvention());
    CompilationLevel level = flags.compilation_level;
    level.setOptionsForCompilationLevel(options);
    if (flags.debug) {
        level.setDebugOptionsForCompilationLevel(options);
    }
    WarningLevel wLevel = flags.warning_level;
    wLevel.setOptionsForWarningLevel(options);
    for (FormattingOption formattingOption : flags.formatting) {
        formattingOption.applyToOptions(options);
    }
    if (flags.process_closure_primitives) {
        options.setProcessClosurePrimitives(true); //fix: setProcessClosurePrimitives should be used instead of closurePass
    }
    initOptionsFromFlags(options);
    return options;
}