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

package gov.nist.ngac.pm.core.pap.pml.operation.resource;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Objects;

/**
 * The runtime form of a resource operation defined in PML.
 *
 * @param <T> the operation's return type
 */
public class PMLStmtsResourceOperation<T> extends PMLResourceOperation<T> implements PMLStatementSerializable {

    private final PMLStatementBlock body;

    public PMLStmtsResourceOperation(String name,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> formalParameters,
                                  List<RequiredCapability> requiredCapabilities,
                                  PMLStatementBlock body) {
        super(name, returnType, formalParameters, requiredCapabilities);
        this.body = body;
    }

    public PMLStmtsResourceOperation(String name,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> formalParameters,
                                  List<FormalParameter<?>> eventParameters,
                                  List<RequiredCapability> requiredCapabilities,
                                  PMLStatementBlock body) {
        super(name, returnType, formalParameters, eventParameters, requiredCapabilities);
        this.body = body;
    }

    public PMLStatementBlock getBody() {
        return body;
    }

    @Override
    public T execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        Object result = ctx.executeOperationStatements(this.body.getStmts(), args);

        return getReturnType().cast(result);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
            "%s%s",
            getSignature().toFormattedString(indentLevel),
            body.toFormattedString(indentLevel)
        );
    }

    @Override
    public String toString() {
        return toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PMLStmtsResourceOperation<?> that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body);
    }
}
