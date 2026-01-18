package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.Operation;
import gov.nist.csd.pm.core.common.tx.TxRunner;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLAdminOperation;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.AccessAdjudication;
import gov.nist.csd.pm.core.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.core.pdp.adjudication.ResourceOperationResult;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;

import gov.nist.csd.pm.core.pdp.event.EventContextUtil;
import java.util.*;
import java.util.Map.Entry;

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
    public Object adjudicateResourceOperation(UserContext user, String resourceOperation, Map<String, Object> rawAgs) throws PMException {
        if (!pap.query().operations().getResourceOperationNames().contains(resourceOperation)) {
            throw new OperationDoesNotExistException(resourceOperation);
        }

        ResourceOperation<?> op = pap.query().operations().getResourceOperation(resourceOperation);
        Args args = op.validateAndPrepareArgs(rawAgs);

        return runTx(user, tx -> executeOperation(user, tx, op, args));
    }

    @Override
    public Object adjudicateAdminOperation(UserContext user,
                                           String operation,
                                           Map<String, Object> rawArgs) throws
                                                                     PMException {
        AdminOperation<?> adminOperation = pap.query().operations().getAdminOperation(operation);
        Args args = adminOperation.validateAndPrepareArgs(rawArgs);

        return runTx(user, tx -> executeOperation(user, tx, adminOperation, args));
    }

    @Override
    public Object adjudicateAdminRoutine(UserContext user,
                                         String routineName,
                                         Map<String, Object> rawArgs) throws PMException {
        Routine<?> routine = pap.query().routines().getAdminRoutine(routineName);
        Args args = routine.validateAndPrepareArgs(rawArgs);

        return runTx(user, tx -> {
            if (routine instanceof PMLRoutine) {
                ((PMLRoutine) routine).setCtx(tx.buildExecutionContext(user));
            }

            return tx.executeFunction(routine, args);
        });
    }

    @Override
    public void adjudicateAdminRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException {
        runTx(user, tx -> {
            for (OperationRequest request : operationRequests) {
                AdminOperation<?> op = pap.query().operations().getAdminOperation(request.op());
                Args args = op.validateAndPrepareArgs(request.args());
                executeOperation(user, tx, op, args);
            }

            return null;
        });
    }

    private ResourceOperationResult buildResourceOperationResult(Args args) throws PMException {
        ResourceOperationResult resourceOperationResult = new ResourceOperationResult();

        for (Entry<FormalParameter<?>, Object> arg : args.getMap().entrySet()) {
            FormalParameter<?> param = arg.getKey();
            Object value = arg.getValue();

            GraphQuery graph = pap.query().graph();
            List<Node> nodes = switch (param) {
                case NodeIdFormalParameter nodeIdFormalParameter ->
                    List.of(graph.getNodeById((long) value));
                case NodeIdListFormalParameter nodeIdFormalParameter -> {
                    List<Node> nodesFromIds = new ArrayList<>();
                    for (long id : (List<Long>) value) {
                        nodesFromIds.add(graph.getNodeById(id));
                    }

                    yield nodesFromIds;
                }
                case NodeNameFormalParameter nodeIdFormalParameter ->
                    List.of(graph.getNodeByName((String) value));
                case NodeNameListFormalParameter nodeIdFormalParameter -> {
                    List<Node> nodesFromNames = new ArrayList<>();
                    for (String name : (List<String>) value) {
                        nodesFromNames.add(graph.getNodeByName(name));
                    }

                    yield nodesFromNames;
                }
                default -> null;
            };

            if (nodes == null) {
                continue;
            }

            resourceOperationResult.addResult(param.getName(), nodes);
        }

        return resourceOperationResult;
    }

    private Object executeOperation(UserContext user, PDPTx pdpTx, Operation<?> operation, Args args) throws PMException {
        if (operation instanceof PMLAdminOperation) {
            ((PMLAdminOperation)operation).setCtx(pdpTx.buildExecutionContext(user));
        }

        // execute operation
        Object ret = pdpTx.executeFunction(operation, args);

        // send to EPP
        publishEvent(EventContextUtil.buildEventContext(
            pap,
            user,
            operation.getName(),
            args
        ));

        return ret;
    }

}
