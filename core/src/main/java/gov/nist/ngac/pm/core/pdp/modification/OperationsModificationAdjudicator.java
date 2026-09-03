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

package gov.nist.ngac.pm.core.pdp.modification;

import static gov.nist.ngac.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.Operation.NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.modification.OperationsModification;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;

/**
 * A {@link OperationsModification} that checks the acting user's admin privileges before delegating to
 * the PAP.
 */
public class OperationsModificationAdjudicator extends Adjudicator implements OperationsModification {

    public OperationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        SetResourceAccessRights op = new SetResourceAccessRights();
        Args args = new Args()
            .put(ARSET_PARAM, new ArrayList<>(resourceAccessRights));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args()
            .put(CreateOperationOp.OPERATION_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        DeleteOperationOp op = new DeleteOperationOp();
        Args args = new Args()
            .put(NAME_PARAM, name);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }
}
