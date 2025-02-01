package gov.nist.csd.pm.common.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.OperandsDoNotMatchException;
import gov.nist.csd.pm.common.executable.AdminExecutable;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.*;

public abstract class Operation<T> extends AdminExecutable<T> {

    public static final String NAME_OPERAND = "name";
    public static final String NODE_OPERAND = "node";

    protected List<String> nodeOperandNames;

    public Operation(String name, List<String> allOperandsNames, List<String> nodeOperandNames) {
        super(name, allOperandsNames);

        validateOperandNames(allOperandsNames, nodeOperandNames);

        this.nodeOperandNames = nodeOperandNames;
    }

    public Operation(String name, List<String> allOperandsNames) {
        super(name, allOperandsNames);

        validateOperandNames(allOperandsNames, new ArrayList<>());

        this.nodeOperandNames = new ArrayList<>();
    }

    public Operation(String name) {
        super(name, new ArrayList<>());

        this.nodeOperandNames = new ArrayList<>();
    }

    private void validateOperandNames(List<String> allOperandsNames, List<String> nodeOperandNames) {
        if (!allOperandsNames.containsAll(nodeOperandNames)) {
            throw new IllegalArgumentException("all nodeOperands must be defined in allOperands");
        }
    }

    public List<String> getNodeOperandNames() {
        return nodeOperandNames;
    }

    public abstract void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException;

    public PreparedOperation<T> withOperands(Map<String, Object> operandValues) throws OperandsDoNotMatchException {
        validateOperands(operandValues);

        return new PreparedOperation<>(this, operandValues);
    }

    public void validateOperands(Map<String, Object> operandValues) throws OperandsDoNotMatchException {
        Set<String> actualOperandNames = operandValues.keySet();

        if (actualOperandNames.size() != operandNames.size()) {
            throw new OperandsDoNotMatchException(name, operandNames, actualOperandNames);
        }

        for (String actualOperandName : actualOperandNames) {
            if (!operandNames.contains(actualOperandName)) {
                throw new OperandsDoNotMatchException(name, operandNames, actualOperandNames);
            }
        }

        for (String nodeOperandName : nodeOperandNames) {
            Object operandValue = operandValues.get(nodeOperandName);
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
    public String toString() {
        return "Operation{" +
                "name='" + name + '\'' +
                ", operandNames=" + operandNames +
                '}';
    }
}
