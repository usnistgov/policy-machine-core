package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.explain.Explain;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;

import java.util.Objects;

public class AdminAdjudicationResponse {

    private Decision decision;
    private Object value;
    private Explain explain;

    public AdminAdjudicationResponse(Decision decision) {
        this.decision = decision;
    }

    public AdminAdjudicationResponse(Decision decision, Object value) {
        this.decision = decision;
        this.value = value;
    }

    public AdminAdjudicationResponse(UnauthorizedException e) {
        this.decision = Decision.DENY;
        this.explain = e.getExplain();
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public Explain getExplain() {
        return explain;
    }

    public void setExplain(Explain explain) {
        this.explain = explain;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminAdjudicationResponse response)) return false;
	    return decision == response.decision && Objects.equals(value, response.value) && Objects.equals(explain, response.explain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decision, value, explain);
    }

    @Override
    public String toString() {
        return "AdminAdjudicationResponse{" +
                "decision=" + decision +
                ", value=" + value +
                ", explain=" + explain +
                '}';
    }
}
