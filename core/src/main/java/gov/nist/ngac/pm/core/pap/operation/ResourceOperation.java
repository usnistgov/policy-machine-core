/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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