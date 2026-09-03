/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.event.EventPublisher;
import gov.nist.ngac.pm.core.common.event.EventSubscriber;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.TxRunner;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.adjudication.AccessAdjudication;
import gov.nist.ngac.pm.core.pdp.adjudication.OperationRequest;
import gov.nist.ngac.pm.core.pdp.bootstrap.PolicyBootstrapper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An administrative PDP that wraps a {@link PAP} with privilege checks on administrative
 * operations.
 */
public class PDP implements EventPublisher, AccessAdjudication {

    protected final PAP pap;
    protected final List<EventSubscriber> eventSubscribers;

    public PDP(PAP pap) {
        this.pap = pap;
        this.eventSubscribers = new CopyOnWriteArrayList<>();
    }

    /**
     * Runs a transaction as the given user.
     *
     * @param userCtx the user
     * @param txRunner the tx runner to execute the transaction
     * @return the tx runner's result
     * @throws PMException if the transaction fails
     */
    public <T> T runTx(UserContext userCtx, PDPTxRunner<T> txRunner) throws PMException {
        return TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, pap, eventSubscribers);
            return txRunner.run(pdpTx);
        });
    }

    /**
     * Executes PML on behalf of the user, in a single transaction.
     *
     * @param userCtx the user
     * @param pml the PML to execute
     * @return the value returned by the PML, or null
     * @throws PMException if execution fails
     */
    public Object executePML(UserContext userCtx, String pml) throws PMException {
        return runTx(userCtx, tx -> tx.executePML(pml));
    }

    /**
     * Bootstraps the underlying PAP's policy via the given bootstrapper.
     */
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
    public Object adjudicateOperation(UserContext user, String resourceOperation, Map<String, Object> rawAgs) throws PMException {
        Operation<?> op = pap.query().operations().getOperation(resourceOperation);
        Args args = op.validateArgs(rawAgs);

        return runTx(user, tx -> executeOperation(user, tx, op, args));
    }

    @Override
    public void adjudicateRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException {
        runTx(user, tx -> {
            for (OperationRequest request : operationRequests) {
                Operation<?> op = pap.query().operations().getOperation(request.op());
                Args args = op.validateArgs(request.args());
                executeOperation(user, tx, op, args);
            }

            return null;
        });
    }

    private Object executeOperation(UserContext user, PDPTx pdpTx, Operation<?> operation, Args args) throws PMException {
        if (operation instanceof PMLOperation) {
            ((PMLOperation)operation).setCtx(pdpTx.buildExecutionContext(user));
        }

        // execute operation
        return pdpTx.executeOperation(operation, user, args);
    }

}
