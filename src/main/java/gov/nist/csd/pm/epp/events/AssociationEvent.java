package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class AssociationEvent extends EventContext {

    private Node source;
    private Node target;

    public AssociationEvent(UserContext userCtx, Node source, Node target) {
        super(userCtx, "association", target);

        this.source = source;
        this.target = target;
    }

    public Node getSource() {
        return source;
    }

    @Override
    public Node getTarget() {
        return target;
    }
}
