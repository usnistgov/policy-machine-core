package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public abstract non-sealed class QueryOperation<R> extends Operation<R> {

    public QueryOperation(String name,
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
