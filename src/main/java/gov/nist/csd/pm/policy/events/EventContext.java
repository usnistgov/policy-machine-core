package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.review.PolicyReview;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class EventContext extends PolicyEvent {

    private final UserContext userCtx;
    private final String eventName;
    private final String target;
    private final PolicyEvent event;

    public EventContext(UserContext userCtx, String target, PolicyEvent event) {
        this.userCtx = userCtx;
        this.eventName = getEventName(event);
        this.target = target;
        this.event = event;
    }

    public EventContext(UserContext userCtx, PolicyEvent event) {
        this.userCtx = userCtx;
        this.eventName = getEventName(event);
        this.target = "";
        this.event = event;
    }

    public UserContext getUserCtx() {
        return userCtx;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTarget() {
        return target;
    }

    public PolicyEvent getEvent() {
        return event;
    }

    public boolean matchesPattern(EventPattern pattern, PolicyReview policyReview) throws PMException {
        if (pattern.getOperations().isEmpty() || pattern.getOperations().get(0).isEmpty()) {
            return true; // an empty event pattern will match all events
        } else if (pattern.getOperations() != null &&
                !pattern.getOperations().contains(eventName)) {
            return false;
        }

        EventSubject patternSubject = pattern.getSubject();
        Target patternTarget = pattern.getTarget();

        return patternSubject.matches(userCtx, policyReview) &&
                patternTarget.matches(target, policyReview);
    }

    private String getEventName(PolicyEvent event) {
        String eventName = "";

        if (event instanceof CreatePolicyClassEvent) {
            eventName = CREATE_POLICY_CLASS;
        } else if (event instanceof CreateObjectAttributeEvent) {
            eventName = CREATE_OBJECT_ATTRIBUTE;
        } else if (event instanceof CreateUserAttributeEvent) {
            eventName = CREATE_USER_ATTRIBUTE;
        } else if (event instanceof CreateObjectEvent) {
            eventName = CREATE_OBJECT;
        } else if (event instanceof CreateUserEvent) {
            eventName = CREATE_USER;
        } else if (event instanceof AssignEvent) {
            eventName = ASSIGN;
        } else if (event instanceof AssignToEvent) {
            eventName = ASSIGN_TO;
        } else if (event instanceof AssociateEvent) {
            eventName = ASSOCIATE;
        } else if (event instanceof CreateObligationEvent) {
            eventName = CREATE_OBLIGATION;
        } else if (event instanceof CreateProhibitionEvent) {
            eventName = CREATE_PROHIBITION;
        } else if (event instanceof DeassignEvent) {
            eventName = DEASSIGN;
        } else if (event instanceof DeassignFromEvent) {
            eventName = DEASSIGN_FROM;
        } else if (event instanceof DeleteNodeEvent) {
            eventName = DELETE_NODE;
        } else if (event instanceof DeleteObligationEvent) {
            eventName = DELETE_OBLIGATION;
        } else if (event instanceof DeleteProhibitionEvent) {
            eventName = DELETE_PROHIBITION;
        } else if (event instanceof DissociateEvent) {
            eventName = DISSOCIATE;
        } else if (event instanceof SetNodePropertiesEvent) {
            eventName = SET_NODE_PROPERTIES;
        } else if (event instanceof SetResourceAccessRightsEvent) {
            eventName = SET_RESOURCE_ACCESS_RIGHTS;
        } else if (event instanceof UpdateObligationEvent) {
            eventName = UPDATE_OBLIGATION;
        } else if (event instanceof UpdateProhibitionEvent) {
            eventName = UPDATE_PROHIBITION;
        }

        return eventName;
    }

    @Override
    public String toString() {
        return "EventContext{" +
                "userCtx=" + userCtx +
                ", eventName='" + eventName + '\'' +
                ", target=" + target +
                ", event=" + event +
                '}';
    }
}
