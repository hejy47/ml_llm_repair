private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    // skip the function name
    arguments.next();
    Iterator<Node> parameters = functionType.getParameters().iterator();
    int ordinal = 0;
    Node parameter = null;
    Node argument = null;
    while (arguments.hasNext() && parameters.hasNext()) {
        // If there are no parameters left in the list, then the while loop
        // above implies that this must be a var_args function.
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
    } else if (maxArgs == Integer.MAX_VALUE && numArgs > minArgs && !functionType.isVarArgs()) {
        // if there is no maximum argument count and the number of arguments exceeds the minimum count and the function is not var_args
        Node traversalTarget = argument != null ? argument : call.getFirstChild();
        validator.expectExactlyNArguments(t, functionType, numArgs, traversalTarget);
    }
}