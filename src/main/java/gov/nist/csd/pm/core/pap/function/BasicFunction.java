package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import java.util.List;

public abstract class BasicFunction<R> extends AdminOperation<R>{

    public BasicFunction(String name,
                         Type<R> returnType,
                         List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }

    protected abstract R execute(Args args) throws PMException;

    @Override
    public final R execute(PAP pap, Args args) throws PMException {
        return execute(args);
    }
}
