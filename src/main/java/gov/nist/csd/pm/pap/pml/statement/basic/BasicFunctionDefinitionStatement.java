package gov.nist.csd.pm.pap.pml.statement.basic;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.basic.PMLStmtsBasicFunction;
import gov.nist.csd.pm.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import java.util.Objects;

public class BasicFunctionDefinitionStatement implements FunctionDefinitionStatement {

    private final PMLStmtsBasicFunction function;
    private final PMLFunctionSignature signature;

    public BasicFunctionDefinitionStatement(PMLStmtsBasicFunction function) {
        this.function = function;
        this.signature = new PMLBasicFunctionSignature(
            function.getName(),
            function.getReturnType(),
            function.getPmlFormalArgs()
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
            "%s%s",
            signature.toFormattedString(indentLevel),
            function.getStatements().toFormattedString(indentLevel)
        );
    }

    @Override
    public VoidValue execute(ExecutionContext ctx, PAP pap) throws PMException {
        ctx.scope().addFunction(function.getName(), function);

        return new VoidValue();
    }

    @Override
    public PMLFunctionSignature getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicFunctionDefinitionStatement that)) return false;
        return Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), function);
    }
}
