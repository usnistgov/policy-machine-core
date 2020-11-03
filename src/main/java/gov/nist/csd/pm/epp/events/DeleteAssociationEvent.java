package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class DeleteAssociationEvent extends EventContext {

    private Node source;
    private Node target;

    public DeleteAssociationEvent(UserContext userCtx, Node source, Node target) {
        super(userCtx, "delete association", target);

        this.source = source;
        this.target = target;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    @Override
    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }
}
