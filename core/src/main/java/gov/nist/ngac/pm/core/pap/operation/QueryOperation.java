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
 * Base class for query operations: {@link Operation}s restricted to read-only {@link PolicyQuery} access.
 *
 * @param <R> the operation's return type
 */
public abstract non-sealed class QueryOperation<R> extends Operation<R> {

    public QueryOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          List<RequiredCapability> requiredCapabilities) {
        super(name, returnType, parameters, requiredCapabilities);
    }

    public QueryOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          RequiredCapability requiredCapability,
                          RequiredCapability... requiredCapabilities) {
        super(name, returnType, parameters, requiredCapability, requiredCapabilities);
    }

    public QueryOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          List<FormalParameter<?>> eventParameters,
                          List<RequiredCapability> requiredCapabilities) {
        super(name, returnType, parameters, eventParameters, requiredCapabilities);
    }

    public QueryOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          List<FormalParameter<?>> eventParameters,
                          RequiredCapability req,
                          RequiredCapability... rest) {
        super(name, returnType, parameters, eventParameters, req, rest);
    }

    /**
     * Computes this operation's result using only read-only policy queries.
     */
    public abstract R execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException;

    @Override
    public final R execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        return execute(pap.query(), userCtx, args);
    }
}
