@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    // Record global variable and function declarations
    if (t.inGlobalScope()) {
        if (NodeUtil.isVarDeclaration(n)) {
            NameInformation ns = createNameInformation(t, n, parent);
            Preconditions.checkNotNull(ns);
            recordSet(ns.name, n);
        } else if (NodeUtil.isFunctionDeclaration(n)) {
            Node nameNode = n.getFirstChild();
            NameInformation ns = createNameInformation(t, nameNode, n);
            if (ns != null) {
                JsName nameInfo = getName(nameNode.getString(), true);
                recordSet(nameInfo.name, n);
            }
        } else if (NodeUtil.isObjectLitKey(n, parent)) {
            NameInformation ns = createNameInformation(t, n, parent);
            if (ns != null) {
                recordSet(ns.name, n);
            }
        }
    }
    // Record assignments and call sites
    if (n.isAssign() && n.getFirstChild().isName()) { // Ensure first child of assignment is a name node
        String name = n.getFirstChild().getString();
        NameInformation ns = createNameInformation(t, n.getFirstChild(), n);
        if (ns != null) {
            if (ns.isPrototype) {
                recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
            } else {
                recordSet(ns.name, n);
            }
        }
    } else if (n.isCall() && n.getFirstChild().isName()) { // Ensure first child of call is a name node
        String name = n.getFirstChild().getString();
        NameInformation ns = createNameInformation(t, n.getFirstChild(), n);
        if (ns != null && ns.onlyAffectsClassDef) {
            JsName jsName = getName(name, false); // Use the string name, not the name node, to retrieve name information
            if (jsName != null) {
                refNodes.add(new ClassDefiningFunctionNode(jsName, n, parent, parent.getParent()));
            }
        }
    }
}