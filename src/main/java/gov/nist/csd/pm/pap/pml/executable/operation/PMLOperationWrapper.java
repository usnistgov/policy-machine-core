package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.arg.FormalArgWrapper;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

public class PMLOperationWrapper extends PMLOperation {

    private final Operation<?> operation;

    public PMLOperationWrapper(Operation<?> operation) {
        super(
            operation.getName(),
            Type.any(),
            FormalArgWrapper.wrap(operation.getFormalArgs())
        );

        this.operation = operation;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
        operation.canExecute(privilegeChecker, userCtx, operands);
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Object o = operation.execute(pap, actualArgs);

        return Value.fromObject(o);
    }
}
