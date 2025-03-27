package gov.nist.csd.pm.pap.pml.statement.basic;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.executable.function.PMLStmtsFunction;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import java.util.Objects;

public class CreateFunctionStatement implements CreateExecutableStatement {

    private final PMLStmtsFunction function;
    private final PMLFunctionSignature signature;

    public CreateFunctionStatement(PMLStmtsFunction function) {
        this.function = function;
        this.signature = new PMLFunctionSignature(
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
        ctx.scope().addExecutable(function.getName(), function);

        return new VoidValue();
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateFunctionStatement that)) return false;
        return Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), function);
    }
}
