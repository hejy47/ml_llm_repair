## Fixed Function 1
@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    // Record global variable and function declarations
    if (t.inGlobalScope() && !n.isFunction()) {
        if (NodeUtil.isVarDeclaration(n)) {
            NameInformation ns = createNameInformation(t, n, parent);
            Preconditions.checkNotNull(ns);
            recordSet(ns.name, n);
        } else if (NodeUtil.isObjectLitKey(n, parent)) {
            NameInformation ns = createNameInformation(t, n, parent);
            if (ns != null) {
                recordSet(ns.name, n);
            }
        }
    }
    if (n.isFunction()) {
        Node nameNode = n.getFirstChild();
        if (NodeUtil.isFunctionDeclaration(n)) {
            NameInformation ns = createNameInformation(t, nameNode, n);
            if (ns != null) {
                JsName nameInfo = getName(nameNode.getString(), true);
                recordSet(nameInfo.name, nameNode);
            }
        }
        // Record assignments and call sites within the function
        NodeTraversal.traverseEs6(compiler, n.getLastChild(), new NodeTraversal.Callback() {
            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.isAssign()) {
                    Node lhs = n.getFirstChild();
                    NameInformation ns = createNameInformation(t, lhs, n);
                    if (ns != null) {
                        if (ns.isPrototype) {
                            recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
                        } else {
                            recordSet(ns.name, lhs);
                        }
                    }
                } else if (n.isCall()) {
                    Node callee = n.getFirstChild();
                    NameInformation ns = null;
                    if (callee.isName()) {
                        ns = createNameInformation(t, callee, n);
                    } else if (callee.isGetProp()) {
                        ns = createNameInformation(t, callee.getLastChild(), n);
                    }
                    if (ns != null && ns.onlyAffectsClassDef) {
                        JsName name = getName(ns.name, false);
                        if (name != null) {
                            refNodes.add(new ClassDefiningFunctionNode(name, n, parent, parent.getParent()));
                        }
                    }
                }
            }
        });
    }
}