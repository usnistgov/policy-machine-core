package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;

import java.io.Serializable;
import java.util.Objects;

public class Rule implements Serializable {

    private String id;
    private EventPattern eventPattern;
    private Response response;

    public Rule() {
    }

    public Rule(String id, EventPattern eventPattern, Response response) {
        this.id = id;
        this.eventPattern = eventPattern;
        this.response = response;
    }

    public Rule(String id, EventSubject subject, Performs performs, Target target, Response response) {
        this.id = id;
        this.eventPattern = new EventPattern(subject, performs, target);
        this.response = response;
    }

    public Rule(String id, EventSubject subject, Performs performs, Response response) {
        this.id = id;
        this.eventPattern = new EventPattern(subject, performs);
        this.response = response;
    }

    public Rule(Rule rule) {
        this.id = rule.id;
        this.eventPattern = new EventPattern(rule.eventPattern);
        this.response = new Response(rule.response);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public void setEventPattern(EventPattern eventPattern) {
        this.eventPattern = eventPattern;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return id.equals(rule.id) && eventPattern.equals(rule.eventPattern) && response.equals(rule.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventPattern, response);
    }
}