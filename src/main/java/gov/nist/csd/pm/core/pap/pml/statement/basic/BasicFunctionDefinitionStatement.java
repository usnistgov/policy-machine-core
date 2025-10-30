package gov.nist.csd.pm.core.pap.pml.statement.basic;

import static gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp.OPERATION_PARAM;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.core.pap.pml.function.basic.PMLStmtsBasicFunction;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.OperationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class BasicFunctionDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    private final PMLStmtsBasicFunction function;

    public BasicFunctionDefinitionStatement(PMLStmtsBasicFunction function) {
        super(new CreateAdminOperationOp());
        this.function = function;
    }

    @Override
    public PMLFunctionSignature getSignature() {
        return function.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args()
            .put(OPERATION_PARAM, function);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        VoidResult value = super.execute(ctx, pap);

        // add function to context
        ctx.scope().addFunction(function.getName(), function);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return function.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasicFunctionDefinitionStatement that)) {
            return false;
        }
        return Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(function);
    }
}
