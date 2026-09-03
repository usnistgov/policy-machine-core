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

import gov.nist.ngac.pm.core.pap.operation.graph.AssignOp;
import gov.nist.ngac.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreatePolicyClassOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.ngac.pm.core.pap.operation.graph.DeassignOp;
import gov.nist.ngac.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.ngac.pm.core.pap.operation.graph.DissociateOp;
import gov.nist.ngac.pm.core.pap.operation.graph.SetNodePropertiesOp;
import gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp;
import gov.nist.ngac.pm.core.pap.operation.obligation.DeleteObligationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.ngac.pm.core.pap.operation.prohibition.CreateNodeProhibitionOp;
import gov.nist.ngac.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp;
import gov.nist.ngac.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import java.util.List;

/**
 * AdminOperations stores a list of the admin operations used in the PolicyModification interfaces.
 */
public class AdminOperations {

    public static List<Operation<?>> ADMIN_OPERATIONS = List.of(
        new AssignOp(),
        new AssociateOp(),
        new CreateObjectAttributeOp(),
        new CreateObjectOp(),
        new CreatePolicyClassOp(),
        new CreateUserAttributeOp(),
        new CreateUserOp(),
        new DeassignOp(),
        new DeleteNodeOp(),
        new DissociateOp(),
        new SetNodePropertiesOp(),

        new CreateObligationOp(),
        new DeleteObligationOp(),

        new SetResourceAccessRights(),
        new CreateOperationOp(),
        new DeleteOperationOp(),

        new CreateNodeProhibitionOp(),
        new CreateProcessProhibitionOp(),
        new DeleteProhibitionOp()
    );

}
