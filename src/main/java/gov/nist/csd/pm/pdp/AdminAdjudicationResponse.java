package gov.nist.csd.pm.pdp;

import java.util.Objects;

public class AdminAdjudicationResponse {

    private Decision decision;

    public AdminAdjudicationResponse(Decision decision) {
        this.decision = decision;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (AdminAdjudicationResponse) obj;
        return Objects.equals(this.decision, that.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decision);
    }

    @Override
    public String toString() {
        return "AdminAdjudicationResponse[" +
                "status=" + decision + ']';
    }

}
