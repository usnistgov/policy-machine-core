package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.event.EventSubscriber;
import gov.nist.csd.pm.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.common.tx.TxRunner;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.AccessAdjudication;
import gov.nist.csd.pm.pdp.adjudication.AdjudicationResponse;
import gov.nist.csd.pm.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.pdp.bootstrap.PolicyBootstrapper;

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
    public Object runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
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
    public AdjudicationResponse adjudicateAdminOperation(UserContext user, String name, Map<String, Object> operands) throws PMException {
        try {
            Object returnValue = runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                return executeOperation(user, ctx, tx, name, operands);
            });

            return new AdjudicationResponse(GRANT, returnValue);
        } catch(UnauthorizedException e){
            return new AdjudicationResponse(e);
        }
    }

    @Override
    public AdjudicationResponse adjudicateAdminRoutine(UserContext user, String name, Map<String, Object> operands) throws PMException {
        Routine<?> adminRoutine = pap.query().routines().getAdminRoutine(name);
        try {
            Object returnValue = runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                if (adminRoutine instanceof PMLRoutine) {
                    ((PMLRoutine) adminRoutine).setCtx(ctx);
                }

                Object o = tx.executeAdminExecutable(adminRoutine, operands);
                if (o instanceof Value value) {
                    return value.toObject();
                }

                return o;
            });

            return new AdjudicationResponse(GRANT, returnValue);
        } catch (UnauthorizedException e) {
            return new AdjudicationResponse(e);
        }
    }

    @Override
    public AdjudicationResponse adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException {
        try {
            runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                for (OperationRequest request : operationRequests) {
                    executeOperation(user, ctx, tx, request.name(), request.operands());
                }

                return null;
            });

            return new AdjudicationResponse(GRANT);
        } catch(UnauthorizedException e){
            return new AdjudicationResponse(e);
        }
    }

    private Object executeOperation(UserContext user, ExecutionContext ctx, PDPTx pdpTx, String name, Map<String, Object> operands) throws PMException {
        Operation<?> operation = pap.query()
                .operations()
                .getAdminOperation(name);

        if (operation instanceof PMLOperation) {
            ((PMLOperation)operation).setCtx(ctx);
        }

        // execute operation
        Object ret = pdpTx.executeAdminExecutable(operation, operands);

        // send to EPP
        publishEvent(new EventContext(
                pap.query().graph().getNodeById(user.getUser()).getName(),
                user.getProcess(),
                operation,
                operands
        ));

        if (ret instanceof Value value) {
            return value.toObject();
        }

        return ret;
    }

}
