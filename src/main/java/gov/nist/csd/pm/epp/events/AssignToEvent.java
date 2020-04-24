package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class AssignToEvent extends EventContext {

    private Node childNode;

    public AssignToEvent(UserContext userCtx, Node target, Node childNode) {
        super(userCtx, ASSIGN_TO_EVENT, target);
        this.childNode = childNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }
}
