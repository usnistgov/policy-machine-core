package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.audit.Explain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AccessReview {

    AccessRightSet getAccessRights(UserContext userCtx, String target) throws PMException;
    AccessRightSet getDeniedAccessRights(UserContext userCtx, String target) throws PMException;
    Map<String, AccessRightSet> getPolicyClassAccessRights(UserContext userContext, String target) throws PMException;
    Map<String, AccessRightSet> buildCapabilityList(UserContext userCtx) throws PMException;
    Map<String, AccessRightSet> buildACL(String target) throws PMException;
    Map<String, AccessRightSet> getBorderAttributes(String user) throws PMException;
    // does not include the root in results
    Map<String, AccessRightSet> getSubgraphAccessRights(UserContext userCtx, String root) throws PMException;
    Explain explain(UserContext userCtx, String target) throws PMException;
    Set<String> buildPOS(UserContext userCtx) throws PMException;
    List<String> getAccessibleChildren(UserContext userCtx, String root) throws PMException;
    List<String> getAccessibleParents(UserContext userCtx, String root) throws PMException;

}
