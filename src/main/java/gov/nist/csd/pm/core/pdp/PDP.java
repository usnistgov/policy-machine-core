package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.TxRunner;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.AccessAdjudication;
import gov.nist.csd.pm.core.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDP extends PDPEventPublisher implements AccessAdjudication {

    protected final PAP pap;

    public PDP(PAP pap) {
        super();
        this.pap = pap;
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
            PDPTx pdpTx = new PDPTx(userCtx, pap, getEpps());
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
    public Object adjudicateOperation(UserContext user, String resourceOperation, Map<String, Object> rawAgs) throws PMException {
        Operation<?> op = pap.query().operations().getOperation(resourceOperation);
        Args args = op.validateAndPrepareArgs(rawAgs);

        return runTx(user, tx -> executeOperation(user, tx, op, args));
    }

    @Override
    public void adjudicateRoutine(UserContext user, List<OperationRequest> operationRequests) throws PMException {
        runTx(user, tx -> {
            for (OperationRequest request : operationRequests) {
                Operation<?> op = pap.query().operations().getOperation(request.op());
                Args args = op.validateAndPrepareArgs(request.args());
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
        return pdpTx.executeOperation(operation, args);
    }

}
