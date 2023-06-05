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
        String maxArgsStr = (maxArgs != Integer.MAX_VALUE) ? " and no more than " + maxArgs + " argument(s)" : "";
        String functionName = getFunctionName(call.getFirstChild());
        report(t, call, WRONG_ARGUMENT_COUNT, functionName, String.valueOf(numArgs), String.valueOf(minArgs), maxArgsStr);
    }
}

// Helper function to extract function name from node
private String getFunctionName(Node n) {
    if (NodeUtil.isName(n)) {
        return n.getString();
    } else if (NodeUtil.isGet(n) && NodeUtil.isQualifiedName(n.getFirstChild())) {
        return NodeUtil.getBestLValueName(n.getFirstChild());
    }
    return "anonymous function";
}