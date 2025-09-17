package gov.nist.csd.pm.core.pap.obligation;

import java.io.Serializable;
import java.util.Objects;

public class Rule implements Serializable {

    private String name;
    private EventPattern eventPattern;
    private ObligationResponse obligationResponse;

    public Rule() {
    }

    public Rule(String name, EventPattern eventPattern, ObligationResponse obligationResponse) {
        this.name = name;
        this.eventPattern = eventPattern;
        this.obligationResponse = obligationResponse;
    }

    public String getName() {
        return name;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public ObligationResponse getResponse() {
        return obligationResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(name, rule.name) && Objects.equals(eventPattern, rule.eventPattern) && Objects.equals(
            obligationResponse, rule.obligationResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventPattern, obligationResponse);
    }
}