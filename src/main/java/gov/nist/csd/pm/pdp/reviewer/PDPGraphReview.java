package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorAccessReview;
import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorGraphReview;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.List;

public class PDPGraphReview implements GraphReview {

    private final AdjudicatorGraphReview adjudicator;
    private final GraphReview graphReview;

    public PDPGraphReview(AdjudicatorGraphReview adjudicator, GraphReview graphReview) {
        this.adjudicator = adjudicator;
        this.graphReview = graphReview;
    }

    @Override
    public List<String> getAttributeContainers(String node) throws PMException {
        adjudicator.getAttributeContainers(node);
        return graphReview.getAttributeContainers(node);
    }

    @Override
    public List<String> getPolicyClassContainers(String node) throws PMException {
        adjudicator.getPolicyClassContainers(node);
        return graphReview.getPolicyClassContainers(node);
    }

    @Override
    public boolean isContained(String subject, String container) throws PMException {
        adjudicator.isContained(subject, container);
        return graphReview.isContained(subject, container);
    }
}
