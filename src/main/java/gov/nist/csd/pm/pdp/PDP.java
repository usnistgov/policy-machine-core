package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventProcessor;
import gov.nist.csd.pm.pap.*;
import gov.nist.csd.pm.pap.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.pap.op.AdminOperations;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.tx.TxRunner;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;

import java.util.*;

import static gov.nist.csd.pm.pap.admin.AdminPolicy.ALL_NODE_NAMES;
import static gov.nist.csd.pm.pap.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.pap.graph.node.Properties.NO_PROPERTIES;

public class PDP implements EventEmitter, AccessAdjudication {

    protected final PAP pap;
    protected final List<EventProcessor> eventProcessors;
    private final PrivilegeChecker privilegeChecker;

    public PDP(PAP pap) {
        this.pap = pap;
        this.eventProcessors = new ArrayList<>();
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
            PDPTx pdpTx = new PDPTx(userCtx, privilegeChecker, pap, eventProcessors);
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
        Set<String> nodes = new HashSet<>(pap.query().graph().search(ANY, NO_PROPERTIES));

        boolean prohibitionsEmpty = pap.query().prohibitions().getProhibitions().isEmpty();
        boolean obligationsEmpty = pap.query().obligations().getObligations().isEmpty();
        boolean resOpsEmpty = pap.query().operations().getResourceOperations().isEmpty();

        Collection<String> adminOperationNames = pap.query().operations().getAdminOperationNames();
        boolean adminOpsEmpty = adminOperationNames.size() == AdminOperations.ADMIN_OP_NAMES.size()
                && adminOperationNames.containsAll(AdminOperations.ADMIN_OP_NAMES);
        boolean routinesEmpty = pap.query().routines().getAdminRoutineNames().isEmpty();

        return (nodes.isEmpty() || (nodes.size() == ALL_NODE_NAMES.size() && nodes.containsAll(ALL_NODE_NAMES))) &&
                prohibitionsEmpty && obligationsEmpty && resOpsEmpty && adminOpsEmpty && routinesEmpty;
    }

    @Override
    public void addEventListener(EventProcessor processor) {
        eventProcessors.add(processor);
    }

    @Override
    public void removeEventListener(EventProcessor processor) {
        eventProcessors.remove(processor);
    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        for (EventProcessor listener : eventProcessors) {
            listener.processEvent(event);
        }
    }

    @Override
    public ResourceAdjudicationResponse adjudicateResourceOperation(UserContext user, String target, String resourceOperation) throws PMException {
        if (!pap.query().operations().getResourceOperations().contains(resourceOperation)) {
            throw new OperationDoesNotExistException(resourceOperation);
        }

        try {
            privilegeChecker.check(user, target, resourceOperation);
        } catch (UnauthorizedException e) {
            return new ResourceAdjudicationResponse(e);
        }

        Node node = pap.query().graph().getNode(target);

        emitEvent(new EventContext(
                user.getUser(),
                user.getProcess(),
                resourceOperation,
                Map.of("target", target)
        ));

        return new ResourceAdjudicationResponse(node);
    }

    private Object executeOperation(UserContext user, ExecutionContext ctx, PDPTx pdpTx, String name, Map<String, Object> operands) throws PMException {
        Operation<?> operation = pap.query()
                .operations()
                .getAdminOperation(name);

        if (operation instanceof PMLOperation) {
            ((PMLOperation)operation).setCtx(ctx);
        }

        Object ret = pdpTx.executeAdminExecutable(operation, operands);

        emitEvent(new EventContext(
                user.getUser(),
                user.getProcess(),
                operation.getName(),
                operands
        ));

        if (ret instanceof Value value) {
            return value.toObject();
        }

        return ret;
    }

    @Override
    public AdminAdjudicationResponse adjudicateAdminOperation(UserContext user, String name, Map<String, Object> operands) throws PMException {
        try {
            Object returnValue = runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                return executeOperation(user, ctx, tx, name, operands);
            });

            return new AdminAdjudicationResponse(Decision.GRANT, returnValue);
        } catch(UnauthorizedException e){
            return new AdminAdjudicationResponse(e);
        }
    }

    @Override
    public AdminAdjudicationResponse adjudicateAdminRoutine(UserContext user, String name, Map<String, Object> operands) throws PMException {
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

            return new AdminAdjudicationResponse(Decision.GRANT, returnValue);
        } catch (UnauthorizedException e) {
            return new AdminAdjudicationResponse(e);
        }
    }

    @Override
    public AdminAdjudicationResponse adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException {
        try {
            runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                for (OperationRequest request : operationRequests) {
                   executeOperation(user, ctx, tx, request.name(), request.operands());
                }

                return null;
            });

            return new AdminAdjudicationResponse(Decision.GRANT);
        } catch(UnauthorizedException e){
            return new AdminAdjudicationResponse(e);
        }
    }
}
