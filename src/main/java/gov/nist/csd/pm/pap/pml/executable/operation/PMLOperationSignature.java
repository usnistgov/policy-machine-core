package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;
import java.util.Map;

public class PMLOperationSignature extends PMLExecutableSignature {

    private final List<String> nodeOperands;

    public PMLOperationSignature(String functionName, Type returnType, List<String> allOperands,
                                 List<String> nodeOperands, Map<String, Type> operandTypes) {
        super(functionName, returnType, allOperands, operandTypes);

        this.nodeOperands = nodeOperands;
    }

    public List<String> getNodeOperands() {
        return nodeOperands;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String argsStr = serializeFormalArgs();
        String indent = indent(indentLevel);
        return String.format(
                "%s%s %s(%s) %s",
                indent,
                "operation",
                functionName,
                argsStr,
                returnType.isVoid() ? "" : returnType.toString() + " "
        );
    }

    @Override
    protected String serializeFormalArgs() {
        String pml = "";
        for (int i = 0; i < operands.size(); i++) {
            String operand = operands.get(i);
            Type operandType = operandTypes.get(operand);

            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += (nodeOperands.contains(operand) ? "@node " : "") +  operandType.toString() + " " + operand;
        }
        return pml;
    }
}
