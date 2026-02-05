package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public abstract non-sealed class ResourceOperation<T> extends Operation<T> {

    public ResourceOperation(String name,
                             Type<T> returnType,
                             List<FormalParameter<?>> parameters,
                             List<RequiredCapability> requiredCapabilities) {
        super(name, returnType, parameters, requiredCapabilities);
    }

    public ResourceOperation(String name,
                             Type<T> returnType,
                             List<FormalParameter<?>> parameters,
                             RequiredCapability... requiredCapabilities) {
        super(name, returnType, parameters, requiredCapabilities);
    }

    public abstract T execute(PolicyQuery query, Args args) throws PMException;

    @Override
    public final T execute(PAP pap, Args args) throws PMException {
        return execute(pap.query(), args);
    }
}