package gov.nist.csd.pm.common.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.OperandsDoNotMatchException;
import gov.nist.csd.pm.common.executable.AdminExecutable;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.*;

public abstract class Operation<T> extends AdminExecutable<T> {

    public static final String NAME_OPERAND = "name";
    public static final String ID_OPERAND = "id";

    // Operands that will be available in an EventContext
    protected List<String> eventCtxOperandNames;

    public Operation(String name, List<String> allOperands, List<String> eventCtxOperandNames) {
        super(name, allOperands);

        validateOperandNames(allOperands, eventCtxOperandNames);

        this.eventCtxOperandNames = eventCtxOperandNames;
    }

    public Operation(String name, List<String> allOperands) {
        super(name, allOperands);

        validateOperandNames(allOperands, new ArrayList<>());

        this.eventCtxOperandNames = new ArrayList<>();
    }

    public Operation(String name) {
        super(name, new ArrayList<>());

        this.eventCtxOperandNames = new ArrayList<>();
    }

    private void validateOperandNames(List<String> allOperands, List<String> nodeOperands) {
        if (!allOperands.containsAll(nodeOperands)) {
            throw new IllegalArgumentException("all nodeOperands must be defined in allOperands");
        }
    }

    public List<String> getEventCtxOperandNames() {
        return eventCtxOperandNames;
    }

    public abstract void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException;

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

        for (String nodeOperandName : eventCtxOperandNames) {
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

            throw new IllegalArgumentException("eventCtx operand can only be a string or collection of strings");
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
