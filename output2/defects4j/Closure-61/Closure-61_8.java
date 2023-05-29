static boolean functionCallHasSideEffects(Node callNode, @Nullable AbstractCompiler compiler) {
    if (callNode.getToken() != Token.CALL) {
        throw new IllegalStateException("Expected CALL node, got " + callNode.getToken());
    }
    if (callNode.isNoSideEffectCall()) {
      return false;
    }
    Node nameNode = callNode.getFirstChild();
    if (!nameNode.isQualifiedName()) {
      return true;
    }
    String name = nameNode.getQualifiedName();
    if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name)) {
      return false;
    }
    if (OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(name) && callNode.hasOneChild()) {
      Node child = callNode.getSecondChild();
      if (child != null && child.isString()) {
        String prop = child.getString();
        if (prop.equals("toString") || prop.equals("valueOf")) {
          return false;
        }
      }
    }
    if (name.equals("String") && callNode.hasOneChild() && callNode.getFirstChild().isTemplateLit()) {
      return false;
    }
    if (name.equals("Math") && callNode.hasTwoChildren()) {
      Node methodNameNode = nameNode.getNext();
      if (methodNameNode != null && methodNameNode.isString()) {
        String methodName = methodNameNode.getString();
        if (MATH_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(methodName)) {
          return false;
        }
      }
    }
    return true;
}