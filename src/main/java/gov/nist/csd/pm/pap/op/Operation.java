package gov.nist.csd.pm.pap.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.OperandsDoNotMatchException;
import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.query.UserContext;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public abstract class Operation<T> extends AdminExecutable<T> implements Serializable {

    public static final String NAME_OPERAND = "name";

    protected List<String> nodeOperands;

    public Operation(String name, List<String> allOperands, List<String> nodeOperands) {
        super(name, allOperands);

        validateOperandNames(allOperands, nodeOperands);

        this.nodeOperands = nodeOperands;
    }

    public Operation(String name, List<String> allOperands) {
        super(name, allOperands);

        validateOperandNames(allOperands, new ArrayList<>());

        this.nodeOperands = new ArrayList<>();
    }

    public Operation(String name) {
        super(name, new ArrayList<>());

        this.nodeOperands = new ArrayList<>();
    }

    private void validateOperandNames(List<String> allOperands, List<String> nodeOperands) {
        if (!allOperands.containsAll(nodeOperands)) {
            throw new IllegalArgumentException("all nodeOperands must be defined in allOperands");
        }
    }

    public List<String> getNodeOperands() {
        return nodeOperands;
    }

    public abstract void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException;

    public PreparedOperation<T> withOperands(Map<String, Object> actualOperands) throws OperandsDoNotMatchException {
        validateOperands(actualOperands);

        return new PreparedOperation<>(this, actualOperands);
    }

    public void validateOperands(Map<String, Object> actualOperands) throws OperandsDoNotMatchException {
        Set<String> actualOperandNames = actualOperands.keySet();

        if (actualOperandNames.size() != operandNames.size()) {
            throw new OperandsDoNotMatchException(name, operandNames, actualOperandNames);
        }

        for (String actualOperandName : actualOperandNames) {
            if (!operandNames.contains(actualOperandName)) {
                throw new OperandsDoNotMatchException(name, operandNames, actualOperandNames);
            }
        }

        for (String nodeOperandName : nodeOperands) {
            Object operandValue = actualOperands.get(nodeOperandName);
            if (operandValue instanceof String) {
                continue;
            }

            if (operandValue instanceof Collection<?> collection) {
                if (collection.isEmpty()) {
                    continue;
                }

                Object next = collection.iterator().next();
                if (next instanceof String) {
                    continue;
                }
            }

            throw new IllegalArgumentException("node operand can only be a string or collection of strings");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation<?> operation = (Operation<?>) o;
        return Objects.equals(name, operation.name) && Objects.equals(operandNames, operation.operandNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, operandNames);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "name='" + name + '\'' +
                ", operandNames=" + operandNames +
                '}';
    }
}
