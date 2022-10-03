package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.events.EventContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;

import java.util.List;

public interface ObligationsReview {

    List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException;
    List<Obligation> getObligationsWithAttributeInEvent(String attribute) throws PMException;
    List<Obligation> getObligationsWithAttributeInResponse(String attribute) throws PMException;
    List<Obligation> getObligationsWithEvent(String event) throws PMException;
    List<Response> getMatchingEventResponses(EventContext eventCtx) throws PMException;

}
