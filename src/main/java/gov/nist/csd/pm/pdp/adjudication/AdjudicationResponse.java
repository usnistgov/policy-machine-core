package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.pap.query.model.explain.Explain;
import gov.nist.csd.pm.pdp.UnauthorizedException;

import java.util.Objects;

public class AdjudicationResponse<T> {

    private Decision decision;
    private T value;
    private Explain explain;

    public AdjudicationResponse(Decision decision) {
        this.decision = decision;
    }

    public AdjudicationResponse(Decision decision, T value) {
        this.decision = decision;
        this.value = value;
    }

    public AdjudicationResponse(UnauthorizedException e) {
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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdjudicationResponse<?> response)) return false;
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
