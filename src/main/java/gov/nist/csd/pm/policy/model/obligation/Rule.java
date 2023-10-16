package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.subject.Subject;
import gov.nist.csd.pm.policy.model.obligation.event.target.Target;

import java.io.Serializable;
import java.util.Objects;

public class Rule implements Serializable {

    private String name;
    private EventPattern eventPattern;
    private Response response;

    public Rule() {
    }

    public Rule(String name, EventPattern eventPattern, Response response) {
        this.name = name;
        this.eventPattern = eventPattern;
        this.response = response;
    }

    public Rule(String name, Subject subject, Performs performs, Target target, Response response) {
        this.name = name;
        this.eventPattern = new EventPattern(subject, performs, target);
        this.response = response;
    }

    public Rule(String name, Subject subject, Performs performs, Response response) {
        this.name = name;
        this.eventPattern = new EventPattern(subject, performs);
        this.response = response;
    }

    public Rule(Rule rule) {
        this.name = rule.name;
        this.eventPattern = new EventPattern(rule.eventPattern);
        this.response = new Response(rule.response);
    }

    public String getName() {
        return name;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;

        return name.equals(rule.name) && eventPattern.equals(rule.eventPattern) && response.equals(rule.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventPattern, response);
    }
}