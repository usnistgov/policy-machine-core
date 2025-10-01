package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.function.AdminFunctionExecutor;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.function.arg.NoArgs;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.obligation.ObligationResponse;
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
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.*;

public class PDPTx implements AdminFunctionExecutor {

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

    public ExecutionContext buildExecutionContext(UserContext userCtx) throws PMException {
        return new PDPExecutionContext(userCtx, this.txExecutor);
    }

    @Override
    public <R, A extends Args> R executeAdminFunction(AdminFunction<R, A> adminFunction,
                                                      Map<String, Object> argsMap) throws PMException {
        return this.txExecutor.executeAdminFunction(adminFunction, argsMap);
    }

    public void executePML(String input) throws PMException {
        txExecutor.executePML(input);
    }

    public void reset() throws PMException {
        txExecutor.reset();
    }

    public String serialize(PolicySerializer serializer) throws PMException {
        return txExecutor.serialize(serializer);
    }

    public void deserialize(String input, PolicyDeserializer policyDeserializer) throws PMException {
        txExecutor.deserialize(input, policyDeserializer);
    }

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
        public <R, A extends Args> R executeAdminFunction(AdminFunction<R, A> adminFunction,
                                                          Map<String, Object> argsMap) throws PMException {
            A args = adminFunction.validateAndPrepareArgs(argsMap);

            if (adminFunction instanceof Routine<R, A> routine) {
                return routine.execute(this, args);
            } else if (adminFunction instanceof Operation<R, A> operation) {
                operation.canExecute(pap, userCtx, args);
                return operation.execute(pap, args);
            }

            return adminFunction.execute(pap, args);
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

        public void executePML(String input) throws PMException {
            PMLCompiler pmlCompiler = new PMLCompiler();
            List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pap, input);

            buildExecutionContext(userCtx)
                .executeStatements(stmts, new NoArgs());
        }

        @Override
        public void executePML(UserContext author, String input) throws PMException {
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
                                   Scope<Object, AdminFunction<?, ?>> scope) throws PMException {
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
