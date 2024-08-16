package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMLOperationWrapper extends PMLOperation {

    private Operation<?> operation;

    public PMLOperationWrapper(Operation<?> operation) {
        super(
                operation.getName(),
                Type.any(),
                operation.getOperandNames(),
                operation.getNodeOperands(),
                getTypesFromOperandNames(operation.getOperandNames())
        );

        this.operation = operation;
    }

    public Operation<?> getOperation() {
        return operation;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        operation.canExecute(pap, userCtx, operands);
    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Object o = operation.execute(pap, operands);

        return Value.fromObject(o);
    }

    public static Map<String, Type> getTypesFromOperandNames(List<String> operandNames) {
        Map<String, Type> types = new HashMap<>();
        for (String operandName : operandNames) {
            types.put(operandName, Type.any());
        }

        return types;
    }
}
