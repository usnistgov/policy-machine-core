package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.List;
import java.util.Map;

/**
 * An interface for computing the privileges for an implied user. Unlike the AccessQuery interface which takes in the user
 * as input, this interface forces its implementations to provide the user. This is needed to support users being able to
 * "navigate" their own access state rather than another user's.
 */
public interface SelfAccessQuery {

    AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException;
    List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException;
    AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException;
    SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException;
    Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException;
    Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException;
    Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException;

}
