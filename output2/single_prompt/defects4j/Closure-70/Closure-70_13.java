private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        if (jsDocParameters != null) {
            Node jsDocParameter = jsDocParameters.getFirstChild();
            for (Node astParameter : astParameters.children()) {
                JSType paramType = null;
                if (jsDocParameter != null) {
                    paramType = jsDocParameter.getJSType();
                    jsDocParameter = jsDocParameter.getNext();
                }
                defineSlot(astParameter, functionNode, paramType, true);
            }
        }
    }
}