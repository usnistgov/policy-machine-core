package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class DeassignFromEvent extends EventContext {

    private Node childNode;

    public DeassignFromEvent(UserContext userCtx, Node target, Node childNode) {
        super(userCtx, DEASSIGN_FROM_EVENT, target);
        this.childNode = childNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }
}
