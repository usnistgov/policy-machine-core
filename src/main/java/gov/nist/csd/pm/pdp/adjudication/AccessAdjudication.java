package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public interface AccessAdjudication {

    AdjudicationResponse adjudicateResourceOperation(UserContext user, long targetId, String resourceOperation) throws PMException;
    AdjudicationResponse adjudicateResourceOperation(UserContext user, String targetName, String resourceOperation) throws PMException;
    <T> AdjudicationResponse adjudicateAdminOperation(UserContext user, Operation<T> operation, ActualArgs args) throws PMException;
    <T> AdjudicationResponse adjudicateAdminRoutine(UserContext user, Routine<T> routine, ActualArgs args) throws PMException;
    AdjudicationResponse adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
