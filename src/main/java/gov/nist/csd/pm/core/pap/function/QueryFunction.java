package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public abstract non-sealed class QueryFunction<R> extends Function<PolicyQuery, R> {

    public QueryFunction(String name,
                         Type<R> returnType,
                         List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }
}
