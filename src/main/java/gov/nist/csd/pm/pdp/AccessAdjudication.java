package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public interface AccessAdjudication {

    ResourceAdjudicationResponse adjudicateResourceOperation(UserContext user, String policyElement, String resourceOperation) throws PMException;
    AdminAdjudicationResponse adjudicateAdminOperation(UserContext user, String name, Map<String, Object> operands) throws PMException;
    AdminAdjudicationResponse adjudicateAdminRoutine(UserContext user, String name, Map<String, Object> operands) throws PMException;
    AdminAdjudicationResponse adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
