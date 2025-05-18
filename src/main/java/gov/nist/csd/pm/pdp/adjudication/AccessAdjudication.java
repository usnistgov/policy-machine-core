package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public interface AccessAdjudication {

    AdjudicationResponse adjudicateResourceOperation(UserContext user, long targetId, String resourceOperation) throws PMException;
    AdjudicationResponse adjudicateAdminOperation(UserContext user, String operation, Map<String, Object> args) throws PMException;
    AdjudicationResponse adjudicateAdminRoutine(UserContext user, String routine, Map<String, Object> args) throws PMException;
    AdjudicationResponse adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
