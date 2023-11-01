package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;
import java.util.Map;

public interface ObligationsReview {

    List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException;
    Map<String, List<Rule>> getRulesWithEventSubject(String subject) throws PMException;
    Map<String, List<Rule>> getRulesWithEventTarget(String target) throws PMException;
    List<Response> getMatchingEventResponses(EventContext eventCtx) throws PMException;

}
