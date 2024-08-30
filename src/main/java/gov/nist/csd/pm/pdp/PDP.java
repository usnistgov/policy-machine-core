package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventProcessor;
import gov.nist.csd.pm.pap.*;
import gov.nist.csd.pm.pap.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.query.UserContext;
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

    public void setExplain(boolean explain) {
        privilegeChecker.setExplain(explain);
    }

    public boolean isExplain() {
        return privilegeChecker.isExplain();
    }

    public void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
        TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, privilegeChecker, pap, eventProcessors);
            txRunner.run(pdpTx);
        });
    }

    public void executePML(UserContext userCtx, String pml) throws PMException {
        runTx(userCtx, tx -> tx.executePML(userCtx, pml));
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

        return (nodes.isEmpty() || (nodes.size() == ALL_NODE_NAMES.size() && nodes.containsAll(ALL_NODE_NAMES))) &&
                prohibitionsEmpty &&
                obligationsEmpty;
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
            return new ResourceAdjudicationResponse(Decision.DENY, pap.query().access().explain(user, target));
        }

        Node node = pap.query().graph().getNode(target);

        emitEvent(new EventContext(
                user.getUser(),
                user.getProcess(),
                resourceOperation,
                Map.of("target", target),
                List.of("target")
        ));

        return new ResourceAdjudicationResponse(Decision.GRANT, node);
    }

    @Override
    public AdminAdjudicationResponse adjudicateAdminOperations(UserContext user, List<OperationRequest> requests) throws PMException {
        try {
            runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                for (OperationRequest request : requests) {
                    Operation<?> operation = pap.query()
                            .operations()
                            .getAdminOperation(request.name());

                    if (operation instanceof PMLOperation) {
                        ((PMLOperation)operation).setCtx(ctx);
                    }

                    tx.executeAdminExecutable(operation, request.operands());

                    emitEvent(new EventContext(
                            user.getUser(),
                            user.getProcess(),
                            operation.getName(),
                            request.operands(),
                            operation.getNodeOperands()
                    ));
                }
            });
        } catch(UnauthorizedException e){
            return new AdminAdjudicationResponse(e);
        }

        return new AdminAdjudicationResponse(Decision.GRANT);
    }

    @Override
    public AdminAdjudicationResponse adjudicateAdminRoutine(UserContext user, RoutineRequest request) throws PMException {
        Routine<?> adminRoutine = pap.query().routines().getAdminRoutine(request.name());
        try {
            runTx(user, tx -> {
                PDPExecutionContext ctx = new PDPExecutionContext(user, tx);

                if (adminRoutine instanceof PMLRoutine) {
                    ((PMLRoutine) adminRoutine).setCtx(ctx);
                }

                tx.executeAdminExecutable(adminRoutine, request.operands());
            });
        } catch (UnauthorizedException e) {
            return new AdminAdjudicationResponse(e);
        }

        return new AdminAdjudicationResponse(Decision.GRANT);
    }
}
