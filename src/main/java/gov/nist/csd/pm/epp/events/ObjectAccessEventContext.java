package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class ObjectAccessEventContext extends EventContext {
    public ObjectAccessEventContext(String event, Node target) {
        super(event, target);
    }
}
