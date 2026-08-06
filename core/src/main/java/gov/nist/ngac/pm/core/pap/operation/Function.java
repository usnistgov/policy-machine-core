package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * Base class for pure functions: {@link Operation}s with no access to the PAP or policy state, computing
 * a result from their arguments alone.
 */
public abstract non-sealed class Function<R> extends Operation<R>{

    public Function(String name,
                    Type<R> returnType,
                    List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters, List.of());
    }

    /**
     * Computes this function's result from its arguments.
     */
    public abstract R execute(Args args) throws PMException;

    @Override
    public final R execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        return execute(args);
    }
}
