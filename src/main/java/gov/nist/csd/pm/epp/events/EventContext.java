package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class EventContext {

    public static final String ASSIGN_TO_EVENT = "assign to";
    public static final String ASSIGN_EVENT = "assign";
    public static final String DEASSIGN_FROM_EVENT = "deassign from";
    public static final String DEASSIGN_EVENT = "deassign";
    public static final String ACCESS_DENIED_EVENT = "deassign";

    private UserContext userCtx;
    private String event;
    private Node   target;

    public EventContext(UserContext userCtx, String event, Node target) {
        this.userCtx = userCtx;
        this.event = event;
        this.target = target;
    }

    public UserContext getUserCtx() {
        return userCtx;
    }

    public String getEvent() {
        return event;
    }

    public Node getTarget() {
        return target;
    }
}
