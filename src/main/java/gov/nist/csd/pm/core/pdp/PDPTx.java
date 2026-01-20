package gov.nist.csd.pm.core.pdp;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DESERIALIZE_POLICY;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.RESET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SERIALIZE_POLICY;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.OperationExecutor;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.scope.ExecuteScope;
import gov.nist.csd.pm.core.pap.pml.scope.Scope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.BreakResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.ContinueResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.StatementResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.core.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.core.pdp.modification.PolicyModificationAdjudicator;
import gov.nist.csd.pm.core.pdp.query.PolicyQueryAdjudicator;
import java.util.List;

public class PDPTx implements OperationExecutor {

    private final TxExecutor txExecutor;

    public PDPTx(UserContext userCtx, PAP pap, List<EventSubscriber> epps) throws PMException {
        this.txExecutor = new TxExecutor(userCtx, pap, epps);
    }

    public PolicyModificationAdjudicator modify() {
        return this.txExecutor.modify();
    }

    public PolicyQueryAdjudicator query() {
        return this.txExecutor.query();
    }

    @Override
    public Object executeOperation(Operation<?> operation,
                                   Args args) throws PMException {
        return this.txExecutor.executeOperation(operation, args);
    }

    /**
     * Builds a PML execution context for the given user.
     * @param userCtx The user context to build the execution context for.
     * @throws PMException If an exception occurs building the context.
     */
    public ExecutionContext buildExecutionContext(UserContext userCtx) throws PMException {
        return new PDPExecutionContext(userCtx, this.txExecutor);
    }

    /**
     * Execute a PML string. If the PML returns a value with a "return ..." statement, that value will be returned.
     * If there is not return in the PML, then this method will return null.
     * @param input The input PML string.
     * @return The value returned by the PML, null if there is none.
     * @throws PMException If an exception occurs compiling or executing the PML.
     */
    public Object executePML(String input) throws PMException {
        return txExecutor.executePML(input);
    }

    /**
     * Resets the current policy state.
     * @throws PMException If an exception occurs while resetting the policy.
     */
    public void reset() throws PMException {
        txExecutor.reset();
    }

    /**
     * Serialize the current policy state using the provided serializer.
     * @param serializer The PolicySerialize used to serialize the policy.
     * @return The String representation of the policy.
     * @throws PMException If an exception occurs while serializing the policy.
     */
    public String serialize(PolicySerializer serializer) throws PMException {
        return txExecutor.serialize(serializer);
    }

    /**
     * Deserialize the provided input string with the provided PolicyDeserializer.
     * @param input The input string.
     * @param policyDeserializer The PolicyDeserializer used to deserialize the provided string.
     * @throws PMException If an exception occurs while deserializing the policy.
     */
    public void deserialize(String input, PolicyDeserializer policyDeserializer) throws PMException {
        txExecutor.deserialize(input, policyDeserializer);
    }

    /**
     * Execute the given obligation response using the provided event context.
     * @param eventCtx The EventContext.
     * @param response The obligation response to execute.
     * @throws PMException If an exception occurs while executing the obligation response.
     */
    public void executeObligationResponse(EventContext eventCtx, ObligationResponse response) throws PMException {
        response.execute(txExecutor, txExecutor.userCtx, eventCtx);
    }

    private static class TxExecutor extends PAP {
        final PAP pap;

        private final UserContext userCtx;
        private final PDPEventPublisher eventPublisher;

        private final PolicyModificationAdjudicator pdpModifier;
        private final PolicyQueryAdjudicator pdpQuerier;

        public TxExecutor(UserContext userCtx, PAP pap, List<EventSubscriber> epps) throws PMException {
            super(pap);

            this.userCtx = userCtx;
            this.pap = pap;
            this.eventPublisher = new PDPEventPublisher(epps);
            this.pdpModifier = new PolicyModificationAdjudicator(this.userCtx, this.pap, this.eventPublisher);
            this.pdpQuerier = new PolicyQueryAdjudicator(this.pap, this.userCtx);
        }

        @Override
        public PolicyModificationAdjudicator modify() {
            return pdpModifier;
        }

        @Override
        public PolicyQueryAdjudicator query() {
            return pdpQuerier;
        }

        @Override
        public ExecutionContext buildExecutionContext(UserContext userCtx) throws PMException {
            return new PDPExecutionContext(userCtx, this);
        }

        @Override
        public Object executeOperation(Operation<?> operation, Args args) throws PMException {
            if (operation instanceof Routine<?> routine) {
                return routine.execute(this, args);
            }

            operation.canExecute(pap, userCtx, args);
            return operation.execute(pap, args);
        }

        @Override
        public void reset() throws PMException {
            privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), RESET);

            pap.reset();
        }

        @Override
        public String serialize(PolicySerializer serializer) throws PMException {
            privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), SERIALIZE_POLICY);

            return pap.serialize(serializer);
        }

        @Override
        public void deserialize(String input, PolicyDeserializer policyDeserializer) throws PMException {
            privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), DESERIALIZE_POLICY);

            pap.deserialize(input, policyDeserializer);
        }

        public Object executePML(String input) throws PMException {
            PMLCompiler pmlCompiler = new PMLCompiler();
            List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pap, input);

            StatementResult statementResult = buildExecutionContext(userCtx)
                .executeStatements(stmts, new Args());

            if (statementResult instanceof ReturnResult returnResult) {
                return returnResult.getValue();
            }

            return null;
        }

        @Override
        public Object executePML(UserContext author, String input) throws PMException {
            throw new PMException("not supported by PDPTx, use executePML(String input) instead");
        }

        @Override
        public void beginTx() throws PMException {
            pap.beginTx();
        }

        @Override
        public void commit() throws PMException {
            pap.commit();
        }

        @Override
        public void rollback() throws PMException {
            pap.rollback();
        }
    }

    private static class PDPExecutionContext extends ExecutionContext {

        private final TxExecutor pdpTx;

        public PDPExecutionContext(UserContext author, TxExecutor pdpTx) throws PMException {
            super(author, pdpTx.pap);
            this.pdpTx = pdpTx;
        }

        public PDPExecutionContext(UserContext author,
                                   TxExecutor pdpTx,
                                   Scope<Object, Operation<?>> scope) throws PMException {
            super(author, pdpTx.pap, scope);
            this.pdpTx = pdpTx;
        }

        @Override
        public ExecutionContext copy() throws PMException {
            return new PDPExecutionContext(author, pdpTx, scope);
        }

        @Override
        public ExecutionContext copyWithParentScope() throws PMException {
            return new PDPExecutionContext(
                author,
                pdpTx,
                scope.getParentScope() == null ? new ExecuteScope(pap) : scope.getParentScope().copy()
            );
        }

        @Override
        public StatementResult executeStatements(List<PMLStatement<?>> statements, Args args) throws PMException {
            ExecutionContext copy = writeArgsToScope(args);

            for (PMLStatement<?> statement : statements) {
                Object value = statement.execute(copy, pdpTx);
                if (value instanceof ReturnResult || value instanceof BreakResult || value instanceof ContinueResult) {
                    return (StatementResult) value;
                }
            }

            return new VoidResult();
        }

        @Override
        public Object executeOperationStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
            ExecutionContext copy = writeArgsToScope(args);

            // for operations, we don't want to use the PDPEC, just the normal one
            // to avoid having access checks inside for loops when they call
            // ctx.executeStatements()
            ExecutionContext ctx = new ExecutionContext(copy.author(), pdpTx.pap, copy.scope());

            return ctx.executeOperationStatements(stmts, args);
        }

        @Override
        public Object executeRoutineStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
            StatementResult result = executeStatements(stmts, args);

            if (result instanceof ReturnResult returnResult) {
                return returnResult.getValue();
            }

            return null;
        }
    }
}
