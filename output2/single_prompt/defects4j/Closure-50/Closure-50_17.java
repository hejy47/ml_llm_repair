private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();
    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }
    Node right = callTarget.getNext();
    if (right != null) {
        if (!NodeUtil.isImmutableValue(right)) {
            return n;
        }
    }
    Node arrayNode = callTarget.getFirstChild();
    Node functionName = arrayNode.getNext();
    if ((arrayNode.getType() != Token.ARRAYLIT) || !functionName.getString().equals("join")) {
        return n;
    }
    // "," is the default, it doesn't need to be explicit
    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = Lists.newArrayList();
    StringBuilder sb = null;
    int foldedSize = 0;
    Node prev = null;
    Node elem = arrayNode.getFirstChild();
    // Merges adjacent String nodes.
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
            if (sb == null) {
                sb = new StringBuilder();
            } else {
                sb.append(joinString);
            }
            sb.append(NodeUtil.getArrayElementStringValue(elem));
        } else {
            if (sb != null) {
                Preconditions.checkNotNull(prev);
                // +2 for the quotes.
                foldedSize += sb.length() + 2;
                arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
                sb = null;
            }
            if (elem.getType() == Token.STRING) {
                foldedSize += elem.getString().length();
            } else {
                foldedSize += InlineCostEstimator.getCost(elem);
            }
            arrayFoldedChildren.add(elem);
        }
        prev = elem;
        elem = elem.getNext();
    }
    if (sb != null) {
        Preconditions.checkNotNull(prev);
        // +2 for the quotes.
        foldedSize += sb.length() + 2;
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
    }
    // one for each comma.
    foldedSize += arrayFoldedChildren.size() - 1;
    int originalSize = InlineCostEstimator.getCost(n);
    switch(arrayFoldedChildren.size()) {
        case 0:
            Node emptyStringNode = IR.string("");
            n.replaceWith(emptyStringNode);
            reportCodeChange();
            return emptyStringNode;
        case 1:
            Node foldedStringNode = arrayFoldedChildren.remove(0);
            if (foldedSize > originalSize) {
                return n;
            } else {
                n.replaceWith(foldedStringNode);
                reportCodeChange();
                return foldedStringNode;
            }
        default:
            // No folding could actually be performed.
            if (arrayFoldedChildren.size() == arrayNode.getChildCount()) {
                return n;
            }
            int kJoinOverhead = "[].join()".length();
            foldedSize += kJoinOverhead;
            foldedSize += (right != null) ? InlineCostEstimator.getCost(right) : 0;
            if (foldedSize > originalSize) {
                return n;
            } else {
                Node joinWrap = IR.call(
                    IR.getprop(IR.arraylit(), "join"),
                    NodeUtil.newStringNode(joinString),
                    arrayNode.removeChildren()
                );
                n.replaceWith(joinWrap);
                reportCodeChange();
                return joinWrap;
            }               
    }
}