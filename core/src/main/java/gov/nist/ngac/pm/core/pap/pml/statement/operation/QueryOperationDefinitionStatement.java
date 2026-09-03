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

package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.ngac.pm.core.pap.pml.statement.OperationDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

/**
 * A PML statement that creates a query operation, registering it in the current scope so later
 * statements can call it.
 */
public class QueryOperationDefinitionStatement extends OperationStatement implements OperationDefinitionStatement {

    protected PMLStmtsQueryOperation<?> pmlStmtsQueryOperation;

    public QueryOperationDefinitionStatement(PMLStmtsQueryOperation<?> pmlStmtsQueryOperation) {
        super(new CreateOperationOp());

        this.pmlStmtsQueryOperation = pmlStmtsQueryOperation;
    }

    @Override
    public PMLOperationSignature getSignature() {
        return pmlStmtsQueryOperation.getSignature();
    }

    @Override
    public PMLStmtsQueryOperation<?> getOperation() {
        return pmlStmtsQueryOperation;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args().put(CreateOperationOp.OPERATION_PARAM, pmlStmtsQueryOperation);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        // add operation to policy
        VoidResult value = super.execute(ctx, pap);

        // add operation to scope
        ctx.scope().addOperation(pmlStmtsQueryOperation.getName(), pmlStmtsQueryOperation);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return pmlStmtsQueryOperation.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryOperationDefinitionStatement that)) {
            return false;
        }
        return Objects.equals(pmlStmtsQueryOperation, that.pmlStmtsQueryOperation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pmlStmtsQueryOperation);
    }
}
