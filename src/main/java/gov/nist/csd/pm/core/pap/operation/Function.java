package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public abstract non-sealed class Function<R> extends Operation<R>{

    public Function(String name,
                    Type<R> returnType,
                    List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters, List.of());
    }

    public abstract R execute(Args args) throws PMException;

    @Override
    public final R execute(PAP pap, Args args) throws PMException {
        return execute(args);
    }
}
