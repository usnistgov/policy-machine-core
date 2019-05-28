package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class EventContext {
    private String event;
    private Node   target;

    public EventContext(String event, Node target) {
        this.event = event;
        this.target = target;
    }

    public String getEvent() {
        return event;
    }

    public Node getTarget() {
        return target;
    }
}
