package gov.nist.csd.pm.core.pdp.adjudication;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Map;

public interface AccessAdjudication {

    Node adjudicateResourceOperation(UserContext user, long targetId, String resourceOperation) throws PMException;
    Object adjudicateAdminOperation(UserContext user, String operation, Map<String, Object> args) throws PMException;
    Object adjudicateAdminRoutine(UserContext user, String routine, Map<String, Object> args) throws PMException;
    void adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
