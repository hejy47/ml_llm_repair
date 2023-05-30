private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    // skip the function name
    arguments.next();
    Iterator<Node> parameters = functionType.getParameters().iterator();
    int ordinal = 0;
    Node parameter = null;
    Node argument = null;
    while (parameters.hasNext()) {
        if (!arguments.hasNext()) {
            // If there are no arguments, then the while loop above implies that this must be a var_args function.
            parameter = parameters.next();
            validator.expectArgumentMatchesParameter(t, null, null, getJSType(parameter), call, ordinal + 1);
            ordinal++;
            continue;
        }
        parameter = parameters.next();
        argument = arguments.next();
        ordinal++;
        validator.expectArgumentMatchesParameter(t, argument, getJSType(argument), getJSType(parameter), call, ordinal);
    }
    int numArgs = call.getChildCount() - 1;
    int minArgs = functionType.getMinArguments();
    int maxArgs = functionType.getMaxArguments();
    if (minArgs > numArgs || maxArgs < numArgs) {
        report(t, call, WRONG_ARGUMENT_COUNT, validator.getReadableJSTypeName(call.getFirstChild(), false), String.valueOf(numArgs), String.valueOf(minArgs), maxArgs != Integer.MAX_VALUE ? " and no more than " + maxArgs + " argument(s)" : "");
    }
}