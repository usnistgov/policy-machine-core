package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateResourceOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsResourceOperation;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class ResourceOpDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    protected PMLStmtsResourceOperation<?> pmlResourceOperation;

    public ResourceOpDefinitionStatement(PMLStmtsResourceOperation<?> pmlResourceOperation) {
        super(new CreateResourceOperationOp());

        this.pmlResourceOperation = pmlResourceOperation;
    }

    @Override
    public PMLFunctionSignature getSignature() {
        return pmlResourceOperation.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args().put(CreateResourceOperationOp.OPERATION_PARAM, pmlResourceOperation);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        // add operation to policy
        VoidResult value = super.execute(ctx, pap);

        // add operation to scope
        ctx.scope().addFunction(pmlResourceOperation.getName(), pmlResourceOperation);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return pmlResourceOperation.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AdminOpDefinitionStatement that))
            return false;
        return Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(op);
    }
}
