package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;

public interface AccessAdjudication {

    ResourceAdjudicationResponse adjudicateResourceOperation(UserContext user, String policyElement, String resourceOperation) throws PMException;
    AdminAdjudicationResponse adjudicateAdminOperations(UserContext user, List<OperationRequest> requests) throws PMException;
    AdminAdjudicationResponse adjudicateAdminRoutine(UserContext user, RoutineRequest request) throws PMException;

}
