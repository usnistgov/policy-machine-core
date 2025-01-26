package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.common.exception.OperandsDoNotMatchException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.common.op.PreparedOperation;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PMLOperation extends Operation<Value> {

    private Type returnType;
    private Map<String, Type> operandTypes;
    protected ExecutionContext ctx;

    public PMLOperation(String name,
                        Type returnType,
                        List<String> allOperands,
                        List<String> nodeOperands,
                        Map<String, Type> operandTypes) {
        super(name, allOperands, nodeOperands);

        validateOperands(allOperands, nodeOperands, operandTypes);

        this.returnType = returnType;
        this.operandTypes = operandTypes;
    }

    public PMLOperation(String name,
                        Type returnType,
                        List<String> allOperands,
                        Map<String, Type> operandTypes) {
        super(name, allOperands, new ArrayList<>());

        validateOperands(allOperands, List.of(), operandTypes);

        this.returnType = returnType;
        this.operandTypes = operandTypes;

    }

    public PMLOperation(String name,
                        Type returnType) {
        super(name, new ArrayList<>(), new ArrayList<>());

        this.returnType = returnType;
        this.operandTypes = new HashMap<>();
    }

    private void validateOperands(List<String> allOperands, List<String> nodeOperands, Map<String, Type> operandTypes) {
        if (operandTypes.size() != allOperands.size()) {
            throw new IllegalArgumentException("expected " + allOperands.size() +
                    " operand types but got " + operandTypes.size());
        } else if (!(operandTypes.keySet().containsAll(allOperands) || operandTypes.keySet().containsAll(nodeOperands))) {
            throw new IllegalArgumentException("operand types must contain all defined operands");
        }

        // check each node operand is a string or string array
        for (String nodeOperand : nodeOperands) {
            Type type = operandTypes.get(nodeOperand);
            if (type.isString() || (type.isArray() && type.getArrayElementType().isString())) {
                continue;
            }

            throw new IllegalArgumentException("node operands must be of type string or string array");
        }
    }

    public PMLExecutableSignature getSignature() {
        return new PMLOperationSignature(
                getName(),
                getReturnType(),
                getOperandNames(),
                getNodeOperandNames(),
                getOperandTypes()
        );
    }

    public Type getReturnType() {
        return returnType;
    }

    public Map<String, Type> getOperandTypes() {
        return operandTypes;
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

    @Override
    public PreparedOperation<Value> withOperands(Map<String, Object> operandValues) throws OperandsDoNotMatchException {
        return new PMLPreparedOperation(this, operandValues);
    }
}
