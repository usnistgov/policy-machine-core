package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * Base class for resource {@link Operation}s that act on resources outside the policy,
 * restricted to read-only {@link PolicyQuery} access to the policy itself.
 *
 * @param <T> the operation's return type
 */
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
                             RequiredCapability requiredCapability,
                             RequiredCapability... requiredCapabilities) {
        super(name, returnType, parameters, requiredCapability, requiredCapabilities);
    }

    public ResourceOperation(String name,
                             Type<T> returnType,
                             List<FormalParameter<?>> parameters,
                             List<FormalParameter<?>> eventParameters,
                             List<RequiredCapability> requiredCapabilities) {
        super(name, returnType, parameters, eventParameters, requiredCapabilities);
    }

    public ResourceOperation(String name,
                             Type<T> returnType,
                             List<FormalParameter<?>> parameters,
                             List<FormalParameter<?>> eventParameters,
                             RequiredCapability req,
                             RequiredCapability... rest) {
        super(name, returnType, parameters, eventParameters, req, rest);
    }

    /**
     * Computes this operation's result, with only read-only policy queries available.
     */
    public abstract T execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException;

    @Override
    public final T execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        return execute(pap.query(), userCtx, args);
    }
}