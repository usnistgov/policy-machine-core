package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.common.routine.Routine;

import java.util.List;
import java.util.Map;

public abstract class PMLRoutine extends Routine<Value> {

    private final Type returnType;
    private final Map<String, Type> operandTypes;
    protected ExecutionContext ctx;

    public PMLRoutine(String name, Type returnType, List<String> operandNames, Map<String, Type> operandTypes) {
        super(name, operandNames);

        if (operandTypes.size() != operandNames.size()) {
            throw new IllegalArgumentException("expected " + operandNames.size() +
                    " operand types but got " + operandTypes.size());
        }

        this.returnType = returnType;
        this.operandTypes = operandTypes;
    }

    public PMLRoutineSignature getSignature() {
        return new PMLRoutineSignature(
                getName(),
                getReturnType(),
                getOperandNames(),
                getOperandTypes()
        );
    }

    public Map<String, Type> getOperandTypes() {
        return operandTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public ExecutionContext getCtx() {
        if (ctx == null) {
            throw new IllegalArgumentException("execution context has not been set");
        }

        return ctx;
    }

    public void setCtx(ExecutionContext ctx) {
        this.ctx = ctx;
    }
}
