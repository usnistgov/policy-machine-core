package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public abstract non-sealed class QueryFunction<R> extends Function<R> {

    public QueryFunction(String name,
                         Type<R> returnType,
                         List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }

    protected abstract R execute(PolicyQuery query, Args args) throws PMException;

    @Override
    public final R execute(PAP pap, Args args) throws PMException {
        return execute(pap.query(), args);
    }
}
