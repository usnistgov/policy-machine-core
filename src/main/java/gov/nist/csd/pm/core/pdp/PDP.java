package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.common.tx.TxRunner;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.AccessAdjudication;
import gov.nist.csd.pm.core.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;

import gov.nist.csd.pm.core.pdp.event.EventContextUtil;
import java.util.*;

public class PDP implements EventPublisher, AccessAdjudication {

    protected final PAP pap;
    protected final List<EventSubscriber> eventSubscribers;

    public PDP(PAP pap) {
        this.pap = pap;
        this.eventSubscribers = new ArrayList<>();
    }

    /**
     * Run a transaction as the given user.
     * @param userCtx The user.
     * @param txRunner The tx runner to execute the transaction.
     * @return an Object if the tx runner returns something.
     * @throws PMException if there is an error executing the transaction.
     */
    public <T> T runTx(UserContext userCtx, PDPTxRunner<T> txRunner) throws PMException {
        return TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, pap, eventSubscribers);
            return txRunner.run(pdpTx);
        });
    }

    /**
     * Execute PML on behalf of the user. The entire PML string will be executed in a transaction.
     * @param userCtx the user.
     * @param pml the PML.
     * @throws PMException tf there is an error executing the PML.
     */
    public void executePML(UserContext userCtx, String pml) throws PMException {
        runTx(userCtx, tx -> {
            tx.executePML(pml);
            return null;
        });
    }

    public void bootstrap(PolicyBootstrapper bootstrapper) throws PMException {
        pap.bootstrap(bootstrapper);
    }

    @Override
    public void addEventSubscriber(EventSubscriber processor) {
        eventSubscribers.add(processor);
    }

    @Override
    public void removeEventSubscriber(EventSubscriber processor) {
        eventSubscribers.remove(processor);
    }

    @Override
    public void publishEvent(EventContext event) throws PMException {
        for (EventSubscriber listener : eventSubscribers) {
            listener.processEvent(event);
        }
    }

    @Override
    public Node adjudicateResourceOperation(UserContext user, long policyElementId, String resourceOperation) throws PMException {
        if (!pap.query().operations().getResourceOperations().contains(resourceOperation)) {
            throw new OperationDoesNotExistException(resourceOperation);
        }

        pap.privilegeChecker().check(user, policyElementId, resourceOperation);

        Node node = pap.query().graph().getNodeById(policyElementId);

        publishEvent(new EventContext(
            EventContextUser.fromUserContext(user, pap),
            resourceOperation,
            Map.of("target", node.getName())
        ));

        return node;
    }

    @Override
    public Object adjudicateAdminOperation(UserContext user,
                                           String operation,
                                           Map<String, Object> args) throws
                                                                     PMException {
        return runTx(user, tx -> executeOperation(user, tx, operation, args));
    }

    @Override
    public Object adjudicateAdminRoutine(UserContext user,
                                         String routineName,
                                         Map<String, Object> args) throws PMException {
        Routine<?, ?> routine = pap.query().routines().getAdminRoutine(routineName);

        return runTx(user, tx -> {
            if (routine instanceof PMLRoutine) {
                ((PMLRoutine) routine).setCtx(tx.buildExecutionContext(user));
            }

            return tx.executeAdminFunction(routine, args);
        });
    }

    @Override
    public void adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException {
        runTx(user, tx -> {
            for (OperationRequest request : operationRequests) {
                executeOperation(user, tx, request.op(), request.args());
            }

            return null;
        });
    }

    private Object executeOperation(UserContext user, PDPTx pdpTx, String op, Map<String, Object> actualArgs) throws PMException {
        Operation<?, ?> operation = pap.query().operations().getAdminOperation(op);

        if (operation instanceof PMLOperation) {
            ((PMLOperation)operation).setCtx(pdpTx.buildExecutionContext(user));
        }

        // execute operation
        Object ret = pdpTx.executeAdminFunction(operation, actualArgs);

        // send to EPP
        publishEvent(EventContextUtil.buildEventContext(
            pap,
            user,
            operation.getName(),
            operation.validateAndPrepareArgs(actualArgs)
        ));

        return ret;
    }

}
