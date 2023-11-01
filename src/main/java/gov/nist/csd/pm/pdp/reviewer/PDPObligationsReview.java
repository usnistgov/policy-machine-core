package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorObligationsReview;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.review.ObligationsReview;

import java.util.List;
import java.util.Map;

public class PDPObligationsReview implements ObligationsReview {

    private final AdjudicatorObligationsReview adjudicator;
    private final ObligationsReview obligationsReview;

    public PDPObligationsReview(AdjudicatorObligationsReview adjudicator, ObligationsReview obligationsReview) {
        this.adjudicator = adjudicator;
        this.obligationsReview = obligationsReview;
    }

    @Override
    public List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException {
        adjudicator.getObligationsWithAuthor(userCtx);
        return obligationsReview.getObligationsWithAuthor(userCtx);
    }

    @Override
    public Map<String, List<Rule>> getRulesWithEventSubject(String subject) throws PMException {
        adjudicator.getRulesWithEventSubject(subject);
        return obligationsReview.getRulesWithEventSubject(subject);
    }

    @Override
    public Map<String, List<Rule>> getRulesWithEventTarget(String target) throws PMException {
        adjudicator.getRulesWithEventTarget(target);
        return obligationsReview.getRulesWithEventTarget(target);
    }

    @Override
    public List<Response> getMatchingEventResponses(EventContext eventCtx) throws PMException {
        adjudicator.getMatchingEventResponses(eventCtx);
        return obligationsReview.getMatchingEventResponses(eventCtx);
    }
}
