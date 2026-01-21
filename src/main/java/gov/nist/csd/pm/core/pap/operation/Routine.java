package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public abstract non-sealed class Routine<R> extends Operation<R> {

    public Routine(String name,
                   Type<R> returnType,
                   List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }

    @Override
    public final void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        // routines do not call canExecute since the access checks are executed for each operation
        // in the routine
    }
}
