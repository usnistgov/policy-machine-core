package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class ObjectAccessEvent extends EventContext {
    public ObjectAccessEvent(UserContext userCtx, String event, Node target) {
        super(userCtx, event, target);
    }
}
