package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class QueryOperationDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    protected PMLStmtsQueryOperation<?> pmlStmtsQueryOperation;

    public QueryOperationDefinitionStatement(PMLStmtsQueryOperation<?> pmlStmtsQueryOperation) {
        super(new CreateOperationOp());

        this.pmlStmtsQueryOperation = pmlStmtsQueryOperation;
    }

    @Override
    public PMLOperationSignature getSignature() {
        return pmlStmtsQueryOperation.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args().put(CreateOperationOp.OPERATION_PARAM, pmlStmtsQueryOperation);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        // add operation to policy
        VoidResult value = super.execute(ctx, pap);

        // add operation to scope
        ctx.scope().addOperation(pmlStmtsQueryOperation.getName(), pmlStmtsQueryOperation);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return pmlStmtsQueryOperation.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryOperationDefinitionStatement that)) {
            return false;
        }
        return Objects.equals(pmlStmtsQueryOperation, that.pmlStmtsQueryOperation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pmlStmtsQueryOperation);
    }
}
