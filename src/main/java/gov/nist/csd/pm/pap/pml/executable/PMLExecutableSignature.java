package gov.nist.csd.pm.pap.pml.executable;

import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PMLExecutableSignature implements PMLStatementSerializable {

    protected String functionName;
    protected Type returnType;
    protected List<String> operands;
    protected Map<String, Type> operandTypes;

    public PMLExecutableSignature(String functionName, Type returnType, List<String> operands, Map<String, Type> operandTypes) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.operands = operands;
        this.operandTypes = operandTypes;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<String> getOperands() {
        return operands;
    }

    public Map<String, Type> getOperandTypes() {
        return operandTypes;
    }

    protected String serializeFormalArgs() {
        String pml = "";
        for (int i = 0; i < operands.size(); i++) {
            String operand = operands.get(i);
            Type operandType = operandTypes.get(operand);

            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += operandType.toString() + " " + operand;
        }
        return pml;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        // by default a signature does not need a formatted string, classes that do need a formatted string will override
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PMLExecutableSignature)) return false;
        PMLExecutableSignature signature = (PMLExecutableSignature) o;
        return Objects.equals(functionName, signature.functionName) && Objects.equals(returnType, signature.returnType) && Objects.equals(operands, signature.operands) && Objects.equals(operandTypes, signature.operandTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, returnType, operands, operandTypes);
    }
}
