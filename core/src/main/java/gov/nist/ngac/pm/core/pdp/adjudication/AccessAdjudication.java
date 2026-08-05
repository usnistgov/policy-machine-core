package gov.nist.ngac.pm.core.pdp.adjudication;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Map;

public interface AccessAdjudication {

    Object adjudicateOperation(UserContext user, String operation, Map<String, Object> args) throws PMException;
    void adjudicateRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
