package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.event.EventSubscriber;
import gov.nist.csd.pm.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.common.tx.TxRunner;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.AccessAdjudication;
import gov.nist.csd.pm.pdp.adjudication.AdjudicationResponse;
import gov.nist.csd.pm.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.pdp.bootstrap.PolicyBootstrapper;

import gov.nist.csd.pm.pdp.event.EventContextUtil;
import java.util.*;

import static gov.nist.csd.pm.pdp.adjudication.Decision.GRANT;

public class PDP implements EventPublisher, AccessAdjudication {

    protected final PAP pap;
    protected final List<EventSubscriber> eventSubscribers;
    private final PrivilegeChecker privilegeChecker;

    public PDP(PAP pap) {
        this.pap = pap;
        this.eventSubscribers = new ArrayList<>();
        this.privilegeChecker = new PrivilegeChecker(pap);
    }

    public PrivilegeChecker getPrivilegeChecker() {
        return privilegeChecker;
    }

    public void setExplain(boolean explain) {
        privilegeChecker.setExplain(explain);
    }

    public boolean isExplain() {
        return privilegeChecker.isExplain();
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
            PDPTx pdpTx = new PDPTx(userCtx, privilegeChecker, pap, eventSubscribers);
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
            tx.executePML(userCtx, pml);
            return null;
        });
    }

    public void bootstrap(String bootstrapUser, PolicyBootstrapper bootstrapper) throws PMException {
        pap.bootstrap(bootstrapUser, bootstrapper);
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
    public AdjudicationResponse adjudicateResourceOperation(UserContext user, long policyElementId, String resourceOperation) throws PMException {
        if (!pap.query().operations().getResourceOperations().contains(resourceOperation)) {
            throw new OperationDoesNotExistException(resourceOperation);
        }

        try {
            privilegeChecker.check(user, policyElementId, resourceOperation);
        } catch (UnauthorizedException e) {
            return new AdjudicationResponse(e);
        }

        Node node = pap.query().graph().getNodeById(policyElementId);

        publishEvent(new EventContext(
            pap.query().graph().getNodeById(user.getUser()).getName(),
            user.getProcess(),
            resourceOperation,
            Map.of("target", node.getName())
        ));

        return new AdjudicationResponse(GRANT, node);
    }

    @Override
    public AdjudicationResponse adjudicateResourceOperation(UserContext user,
                                                            String targetName,
                                                            String resourceOperation) throws
                                                                                            PMException {
        return adjudicateResourceOperation(user, pap.query().graph().getNodeId(targetName), resourceOperation);
    }

    @Override
    public <T> AdjudicationResponse adjudicateAdminOperation(UserContext user,
                                                             Operation<T> operation,
                                                             ActualArgs args) throws
                                                                                 PMException {
        try {
            Object returnValue = runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                return executeOperation(user, ctx, tx, operation, args);
            });

            return new AdjudicationResponse(GRANT, returnValue);
        } catch(UnauthorizedException e){
            return new AdjudicationResponse(e);
        }
    }

    @Override
    public <T> AdjudicationResponse adjudicateAdminRoutine(UserContext user,
                                                           Routine<T> routine,
                                                           ActualArgs args) throws PMException {
        try {
            Object returnValue = runTx(user, tx -> {
                if (routine instanceof PMLRoutine) {
                    PDPExecutionContext ctx = new PDPExecutionContext(user, tx);
                    ((PMLRoutine) routine).setCtx(ctx);
                }

                Object ret = tx.executeAdminFunction(routine, args);

                // if the returned object is a value, convert it to an object
                if (ret instanceof Value value) {
                    ret = value.toObject();
                }

                return ret;
            });

            return new AdjudicationResponse(GRANT, returnValue);
        } catch (UnauthorizedException e) {
            return new AdjudicationResponse(e);
        }
    }

    @Override
    public AdjudicationResponse adjudicateAdminRoutine(UserContext user,
                                                       List<OperationRequest> operationRequests) throws
                                                                                                       PMException {
        try {
            runTx(user, tx -> {
                for (OperationRequest request : operationRequests) {
                    PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                    executeOperation(user, ctx, tx, request.op(), request.args());
                }

                return null;
            });

            return new AdjudicationResponse(GRANT);
        } catch(UnauthorizedException e){
            return new AdjudicationResponse(e);
        }
    }

    private <T> Object executeOperation(UserContext user, ExecutionContext ctx, PDPTx pdpTx, Operation<T> operation, ActualArgs args) throws PMException {
        if (operation instanceof PMLOperation) {
            ((PMLOperation)operation).setCtx(ctx);
        }

        // execute operation
        Object ret = pdpTx.executeAdminFunction(operation, args);

        // send to EPP
        publishEvent(EventContextUtil.buildEventContext(
            pap,
            user,
            operation.getName(),
            args
        ));

        // if the returned object is a value, convert it to an object
        if (ret instanceof Value value) {
            ret = value.toObject();
        }

        return ret;
    }

}
