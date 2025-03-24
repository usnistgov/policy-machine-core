package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.ExecuteScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.*;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class PDPExecutionContext extends ExecutionContext {

    private final PDPTx pdpTx;

    public PDPExecutionContext(UserContext author, PDPTx pdpTx) throws PMException {
        super(author, pdpTx.pap);
        this.pdpTx = pdpTx;
    }

    public PDPExecutionContext(UserContext author, PDPTx pdpTx, Scope<Value, AdminExecutable<?>> scope) throws PMException {
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
    public Value executeStatements(List<PMLStatement> statements, Map<String, Object> operands) throws PMException {
        ExecutionContext copy = writeOperandsToScope(operands);

        for (PMLStatement statement : statements) {
            Value value = statement.execute(copy, pdpTx);
            if (value instanceof ReturnValue || value instanceof BreakValue || value instanceof ContinueValue) {
                return value;
            }
        }

        return new VoidValue();
    }

    @Override
    public Value executeOperationStatements(List<PMLStatement> stmts, Map<String, Object> operands) throws PMException {
        ExecutionContext copy = writeOperandsToScope(operands);

        // for operations, we don't want to use the PDPEC, just the normal one
        // to avoid having access checks inside for loops when they call
        // ctx.executeStatements()
        ExecutionContext ctx = new ExecutionContext(copy.author(), pdpTx.pap, copy.scope());
        ctx.setExplain(pdpTx.getPrivilegeChecker().isExplain());

        for (PMLStatement statement : stmts) {
            Value value = statement.execute(ctx, pdpTx.pap);
            if (value instanceof ReturnValue || value instanceof BreakValue || value instanceof ContinueValue) {
                return value;
            }
        }

        return new VoidValue();
    }

    @Override
    public Value executeRoutineStatements(List<PMLStatement> stmts, Map<String, Object> operands) throws PMException {
        return executeStatements(stmts, operands);
    }
}
