package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import java.io.Serializable;
import java.util.Objects;

public class Obligation implements Serializable {

    private long authorId;
    private String name;
    private EventPattern eventPattern;
    private ObligationResponse response;

    public Obligation() {
    }

    public Obligation(long authorId, String name, EventPattern eventPattern, ObligationResponse response) {
        this.authorId = authorId;
        this.name = name;
        this.eventPattern = eventPattern;
        this.response = response;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public void setEventPattern(EventPattern eventPattern) {
        this.eventPattern = eventPattern;
    }

    public ObligationResponse getResponse() {
        return response;
    }

    public void setResponse(ObligationResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return CreateObligationStatement.fromObligation(this).toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Obligation that)) {
            return false;
        }
        return authorId == that.authorId && Objects.equals(name, that.name) && Objects.equals(
            eventPattern, that.eventPattern) && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, name, eventPattern, response);
    }
}