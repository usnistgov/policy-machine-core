package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class AssignToEventContext extends EventContext {

    private Node childNode;

    public AssignToEventContext(String event, Node target, Node childNode) {
        super(event, target);
        this.childNode = childNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }
}
