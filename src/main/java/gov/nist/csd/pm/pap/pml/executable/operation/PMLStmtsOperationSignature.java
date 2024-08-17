package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;
import java.util.Map;

public class PMLStmtsOperationSignature extends PMLOperationSignature{

    private PMLStatementBlock checks;

    public PMLStmtsOperationSignature(
            String functionName,
            Type returnType,
            List<String> allOperands,
            List<String> nodeOperands,
            Map<String, Type> operandTypes,
            PMLStatementBlock checks) {
        super(functionName, returnType, allOperands, nodeOperands, operandTypes);

        this.checks = checks;
    }

    public PMLStatementBlock getChecks() {
        return checks;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String argsStr = serializeFormalArgs();
        String indent = indent(indentLevel);
        return String.format(
                "%s%s %s(%s) %s%s",
                indent,
                "operation",
                functionName,
                argsStr,
                returnType.isVoid() ? "" : returnType.toString() + " ",
                checks.getStmts().isEmpty() ? "" : checks.toFormattedString(indentLevel) + " "
        );
    }
}
