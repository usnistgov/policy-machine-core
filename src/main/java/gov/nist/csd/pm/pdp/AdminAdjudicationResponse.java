package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.query.explain.Explain;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;

import java.util.Objects;

public class AdminAdjudicationResponse {

    private Decision decision;
    private Explain explain;

    public AdminAdjudicationResponse(Decision decision) {
        this.decision = decision;
    }

    public AdminAdjudicationResponse(Decision decision, Explain explain) {
        this.decision = decision;
        this.explain = explain;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminAdjudicationResponse response)) return false;
        return decision == response.decision && Objects.equals(explain, response.explain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decision, explain);
    }

    @Override
    public String toString() {
        return "AdminAdjudicationResponse{" +
                "decision=" + decision +
                ", explain=" + explain +
                '}';
    }
}
