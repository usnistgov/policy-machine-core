package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class AssignToEvent extends EventContext {

    private Node childNode;

    public AssignToEvent(Node target, Node childNode) {
        super(ASSIGN_TO_EVENT, target);
        this.childNode = childNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }
}
