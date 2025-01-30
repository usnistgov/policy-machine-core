package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.event.EventPublishable;
import gov.nist.csd.pm.common.event.operand.OperandValue;
import gov.nist.csd.pm.common.event.operand.StringOperandValue;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.event.EventSubscriber;
import gov.nist.csd.pm.pap.*;
import gov.nist.csd.pm.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.pap.AdminOperations;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.tx.TxRunner;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pdp.adjudication.AccessAdjudication;
import gov.nist.csd.pm.pdp.adjudication.AdjudicationResponse;
import gov.nist.csd.pm.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.pdp.bootstrap.PolicyBootstrapper;

import java.util.*;

import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODES;
import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODE_NAMES;
import static gov.nist.csd.pm.common.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
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

    public Object runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
        return TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, privilegeChecker, pap, eventSubscribers);
            return txRunner.run(pdpTx);
        });
    }

    public void executePML(UserContext userCtx, String pml) throws PMException {
        runTx(userCtx, tx -> {
            tx.executePML(userCtx, pml);
            return null;
        });
    }

    public void bootstrap(PolicyBootstrapper bootstrapper) throws PMException {
        if(!isPolicyEmpty()) {
            throw new BootstrapExistingPolicyException();
        }

        bootstrapper.bootstrap(pap);
    }

    private boolean isPolicyEmpty() throws PMException {
        HashSet<Node> nodes = new HashSet<>(pap.query().graph().search(ANY, NO_PROPERTIES));

        boolean prohibitionsEmpty = pap.query().prohibitions().getProhibitions().isEmpty();
        boolean obligationsEmpty = pap.query().obligations().getObligations().isEmpty();
        boolean resOpsEmpty = pap.query().operations().getResourceOperations().isEmpty();

        Collection<String> adminOperationNames = pap.query().operations().getAdminOperationNames();
        boolean adminOpsEmpty = adminOperationNames.size() == AdminOperations.ADMIN_OP_NAMES.size()
                && adminOperationNames.containsAll(AdminOperations.ADMIN_OP_NAMES);
        boolean routinesEmpty = pap.query().routines().getAdminRoutineNames().isEmpty();

        return (nodes.isEmpty() || (nodes.size() == ALL_NODE_NAMES.size() && nodes.containsAll(ALL_NODES))) &&
                prohibitionsEmpty && obligationsEmpty && resOpsEmpty && adminOpsEmpty && routinesEmpty;
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
                Map.of("target", new StringOperandValue(node.getName()))
        ));

        return new AdjudicationResponse(GRANT, node);
    }

    private Object executeOperation(UserContext user, ExecutionContext ctx, PDPTx pdpTx, String name, Map<String, OperandValue> operands) throws PMException {
        Operation<?> operation = pap.query()
                .operations()
                .getAdminOperation(name);

        if (operation instanceof PMLOperation) {
            ((PMLOperation)operation).setCtx(ctx);
        }

        Object ret = pdpTx.executeAdminExecutable(operation, operands);

        if (operation instanceof EventPublishable eventPublishable) {
            publishEvent(eventPublishable.toEventContext(pap, user, operation, operands));
        }

        if (ret instanceof Value value) {
            return value.toObject();
        }

        return ret;
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
}
