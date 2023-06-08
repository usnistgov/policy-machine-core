package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.graph.node.Node;

import java.util.Objects;

public class ResourceAdjudicationResponse {

    private Decision decision;
    private Node resource;

    public ResourceAdjudicationResponse(Decision decision, Node resource) {
        this.decision = decision;
        this.resource = resource;
    }

    public ResourceAdjudicationResponse(Decision decision) {
        this.decision = decision;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (ResourceAdjudicationResponse) obj;
        return Objects.equals(this.decision, that.decision) &&
                Objects.equals(this.resource, that.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decision, resource);
    }

    @Override
    public String toString() {
        return "ResourceAdjudicationResponse[" +
                "status=" + decision + ", " +
                "node=" + resource + ']';
    }

}
