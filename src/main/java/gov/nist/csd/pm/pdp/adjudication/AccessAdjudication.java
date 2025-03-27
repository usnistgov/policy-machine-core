package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public interface AccessAdjudication {

    AdjudicationResponse<Node>  adjudicateResourceOperation(UserContext user, long targetId, String resourceOperation) throws PMException;
    AdjudicationResponse<Node>  adjudicateResourceOperation(UserContext user, String targetName, String resourceOperation) throws PMException;
    <T> AdjudicationResponse<T> adjudicateAdminOperation(UserContext user, Operation<T> operation, ActualArgs args) throws PMException;
    <T> AdjudicationResponse<T>  adjudicateAdminRoutine(UserContext user, Routine<T> routine, ActualArgs args) throws PMException;
    AdjudicationResponse<Void>  adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException;

}
