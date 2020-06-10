package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class DeassignEvent extends EventContext {

    private Node parentNode;

    public DeassignEvent(UserContext userCtx, Node target, Node parentNode) {
        super(userCtx, DEASSIGN_EVENT, target);
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
}
