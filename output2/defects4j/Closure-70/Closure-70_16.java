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
                    // Check if JSDoc parameter has a name and define slot with type
                    String paramName = jsDocParameter.getString();
                    if (paramName != null) {
                        defineSlot(astParameter, functionNode, jsDocParameter.getJSType(), true);
                    } else {
                        // If the parameter name is null, define slot without type
                        defineSlot(astParameter, functionNode, null, true);
                    }
                    jsDocParameter = jsDocParameter.getNext();
                } else {
                    // If no JSDoc parameter is provided, define slot without type
                    defineSlot(astParameter, functionNode, null, true);
                }
            }
        }
    }
}