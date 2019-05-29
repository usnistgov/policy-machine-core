package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class ObjectAccessEvent extends EventContext {
    public ObjectAccessEvent(String event, Node target) {
        super(event, target);
    }
}
