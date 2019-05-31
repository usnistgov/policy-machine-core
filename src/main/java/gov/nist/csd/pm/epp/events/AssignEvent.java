package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class AssignEvent extends EventContext {

    private Node parentNode;

    public AssignEvent(Node target, Node parentNode) {
        super(ASSIGN_EVENT, target);
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
}
