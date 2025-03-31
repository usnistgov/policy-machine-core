package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionWrapper;
import gov.nist.csd.pm.pap.pml.function.arg.FormalArgWrapper;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.arg.WrappedFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

public class PMLOperationWrapper extends PMLOperation implements PMLFunctionWrapper {

    private final Operation<?> operation;
    private List<WrappedFormalArg<?>> formalArgs;

    public PMLOperationWrapper(Operation<?> operation) {
        super(
            operation.getName(),
            Type.any(),
            new ArrayList<>(FormalArgWrapper.wrap(operation.getFormalArgs()))
        );

        this.operation = operation;
        this.formalArgs = FormalArgWrapper.wrap(operation.getFormalArgs());
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        operation.canExecute(privilegeChecker, userCtx, args);
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        Object o = operation.execute(pap, args);

        return Value.fromObject(o);
    }

    @Override
    public List<WrappedFormalArg<?>> getPMLFormalArgs() {
        return formalArgs;
    }
}
