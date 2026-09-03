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
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;

/**
 * Base class for PML statements that execute a single {@link Operation}.
 */
public abstract class OperationStatement extends PMLStatement<VoidResult> {

    protected Operation<?> op;

    public OperationStatement(Operation<?> op) {
        this.op = op;
    }

    public Operation<?> getOp() {
        return op;
    }

    /**
     * Builds the arguments to invoke this statement's operation with.
     *
     * @param ctx the execution context to resolve expressions against
     * @param pap the PAP to resolve names against
     * @return the arguments to invoke the operation with
     * @throws PMException if resolving an expression or name fails
     */
    public abstract Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        op.execute(pap, ctx.author(), prepareArgs(ctx, pap));

        return new VoidResult();
    }
}
