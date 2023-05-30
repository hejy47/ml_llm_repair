private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        if (jsDocParameters != null) {
            Node jsDocParameter = jsDocParameters.getFirstChild();
            for (Node astParameter : astParameters.children()) {
                if (jsDocParameter != null) {
                    defineSlot(astParameter, functionNode, getJSType(jsDocParameter), true);
                    jsDocParameter = jsDocParameter.getNext();
                } else {
                    defineSlot(astParameter, functionNode, null, true);
                }
            }
        }
    }
}

private JSType getJSType(Node jsDocParameter) {
    if (jsDocParameter.getType() == Token.BANG) {
        return jsDocParameter.getJSType();
    } else {
        return jsDocParameter.getFirstChild().getJSType();
    }
}