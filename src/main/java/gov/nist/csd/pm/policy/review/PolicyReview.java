package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.audit.Explain;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.events.EventContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PolicyReview {

    AccessRightSet getAccessRights(UserContext userCtx, String target) throws PMException;
    AccessRightSet getDeniedAccessRights(UserContext userCtx, String target) throws PMException;
    Map<String, AccessRightSet> getPolicyClassAccessRights(UserContext userContext, String target) throws PMException;
    Map<String, AccessRightSet> buildCapabilityList(UserContext userCtx) throws PMException;
    Map<String, AccessRightSet> buildACL(String target) throws PMException;
    Map<String, AccessRightSet> getBorderAttributes(String user) throws PMException;

    /**
     * does not include the root in results
     * @param userCtx
     * @param root
     * @return
     * @throws PMException
     */
    Map<String, AccessRightSet> getSubgraphAccessRights(UserContext userCtx, String root) throws PMException;
    Explain explain(UserContext userCtx, String target) throws PMException;
    Set<String> buildPOS(UserContext userCtx) throws PMException;
    List<String> getAccessibleChildren(UserContext userCtx, String root) throws PMException;
    List<String> getAccessibleParents(UserContext userCtx, String root) throws PMException;
    List<String> getAttributeContainers(String node) throws PMException;
    List<String> getPolicyClassContainers(String node) throws PMException;
    boolean isContained(String subject, String container) throws PMException;

    List<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException;

    // TODO add function for getting prohibitions with given container

    List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException;
    List<Obligation> getObligationsWithAttributeInEvent(String attribute) throws PMException;
    List<Obligation> getObligationsWithAttributeInResponse(String attribute) throws PMException;
    List<Obligation> getObligationsWithEvent(String event) throws PMException;
    List<Response> getMatchingEventResponses(EventContext eventCtx) throws PMException;

}
