package gov.nist.csd.pm.core.pap.obligation;

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
        return Objects.equals(name, rule.name) && Objects.equals(eventPattern, rule.eventPattern) && Objects.equals(response, rule.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventPattern, response);
    }
}