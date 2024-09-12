package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.query.model.explain.Explain;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;

import java.util.Objects;

public class ResourceAdjudicationResponse {

    private Decision decision;
    private Node resource;
    private Explain explain;

    public ResourceAdjudicationResponse(Node resource) {
        this.decision = Decision.GRANT;
        this.resource = resource;
    }

    public ResourceAdjudicationResponse(UnauthorizedException e) {
        this.decision = Decision.DENY;
        this.explain = e.getExplain();
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public Node getResource() {
        return resource;
    }

    public void setResource(Node resource) {
        this.resource = resource;
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
        if (!(o instanceof ResourceAdjudicationResponse that)) return false;
        return decision == that.decision && Objects.equals(resource, that.resource) && Objects.equals(explain, that.explain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decision, resource, explain);
    }

    @Override
    public String toString() {
        return "ResourceAdjudicationResponse{" +
                "decision=" + decision +
                ", resource=" + resource +
                ", explain=" + explain +
                '}';
    }
}
