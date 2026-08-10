package gov.nist.ngac.pm.core.pdp.adjudication;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Map;

/**
 * Adjudicates a user's request to execute a single operation or a routine's batch of operations,
 * enforcing required privileges before executing.
 */
public interface AccessAdjudication {

    Object adjudicateOperation(UserContext user, String operation, Map<String, Object> args) throws PMException;
    void adjudicateRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
