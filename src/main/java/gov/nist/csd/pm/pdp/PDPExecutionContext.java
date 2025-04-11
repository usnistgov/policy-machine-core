package gov.nist.csd.pm.pdp;

import com.sun.jdi.VoidValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.ExecuteScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.result.BreakResult;
import gov.nist.csd.pm.pap.pml.statement.result.ContinueResult;
import gov.nist.csd.pm.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.pap.pml.statement.result.StatementResult;
import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

class PDPExecutionContext extends ExecutionContext {

    private final PDPTx pdpTx;

    public PDPExecutionContext(UserContext author, PDPTx pdpTx) throws PMException {
        super(author, pdpTx.pap);
        this.pdpTx = pdpTx;
    }

    public PDPExecutionContext(UserContext author, PDPTx pdpTx, Scope<Object, AdminFunction<?, ?>> scope) throws PMException {
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
    public StatementResult executeOperationStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        ExecutionContext copy = writeArgsToScope(args);

        // for operations, we don't want to use the PDPEC, just the normal one
        // to avoid having access checks inside for loops when they call
        // ctx.executeStatements()
        ExecutionContext ctx = new ExecutionContext(copy.author(), pdpTx.pap, copy.scope());
        ctx.setExplain(pdpTx.getPrivilegeChecker().isExplain());

        for (PMLStatement statement : stmts) {
            Object value = statement.execute(ctx, pdpTx.pap);
            if (value instanceof ReturnResult || value instanceof BreakResult || value instanceof ContinueResult) {
                return (StatementResult) value;
            }
        }

        return new VoidResult();
    }

    @Override
    public StatementResult executeRoutineStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        return executeStatements(stmts, args);
    }
}
