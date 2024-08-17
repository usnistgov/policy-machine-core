package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.query.explain.Explain;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface AccessQuery {

    AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException;
    AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException;
    Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userContext, String target) throws PMException;
    Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException;
    Map<String, AccessRightSet> computeACL(String target) throws PMException;
    Map<String, AccessRightSet> computeDestinationAttributes(String user) throws PMException;
    Map<String, AccessRightSet> computeAscendantPrivileges(UserContext userCtx, String root) throws PMException;
    Explain explain(UserContext userCtx, String target) throws PMException;
    Set<String> computePersonalObjectSystem(UserContext userCtx) throws PMException;
    Collection<String> computeAccessibleAscendants(UserContext userCtx, String root) throws PMException;
    Collection<String> computeAccessibleDescendants(UserContext userCtx, String root) throws PMException;

}
