package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorProhibitionsReview;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.review.ProhibitionsReview;

import java.util.List;

public class PDPProhibitionsReview implements ProhibitionsReview {

    private final AdjudicatorProhibitionsReview adjudicator;
    private final ProhibitionsReview prohibitionsReview;

    public PDPProhibitionsReview(AdjudicatorProhibitionsReview adjudicator, ProhibitionsReview prohibitionsReview) {
        this.adjudicator = adjudicator;
        this.prohibitionsReview = prohibitionsReview;
    }

    @Override
    public List<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException {
        adjudicator.getInheritedProhibitionsFor(subject);

        return prohibitionsReview.getInheritedProhibitionsFor(subject);
    }

    @Override
    public List<Prohibition> getProhibitionsWithContainer(String container) throws PMException {
        adjudicator.getProhibitionsWithContainer(container);

        return prohibitionsReview.getProhibitionsWithContainer(container);
    }
}
