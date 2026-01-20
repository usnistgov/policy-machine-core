package gov.nist.csd.pm.core.pap.pml.statement.basic;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.CreateBasicFunctionOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLStmtsBasicOperation;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.OperationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class BasicFunctionDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    private final PMLStmtsBasicOperation<?> function;

    public BasicFunctionDefinitionStatement(PMLStmtsBasicOperation<?> function) {
        super(new CreateBasicFunctionOp());
        this.function = function;
    }

    @Override
    public PMLOperationSignature getSignature() {
        return function.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args()
            .put(CreateBasicFunctionOp.BASIC_FUNCTION_PARAM, function);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        VoidResult value = super.execute(ctx, pap);

        // add function to context
        ctx.scope().addOperation(function.getName(), function);

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
