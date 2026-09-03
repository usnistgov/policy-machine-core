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
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * Base class for basic {@link Operation}s with no access to the PAP or policy state.
 *
 * @param <R> the function's return type
 */
public abstract non-sealed class Function<R> extends Operation<R>{

    public Function(String name,
                    Type<R> returnType,
                    List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters, List.of());
    }

    /**
     * Computes this function's result.
     *
     * @param args the function's argument values
     * @return the function's result
     * @throws PMException if execution fails
     */
    public abstract R execute(Args args) throws PMException;

    @Override
    public final R execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        return execute(args);
    }
}
