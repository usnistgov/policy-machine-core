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

package gov.nist.ngac.pm.core.pdp;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class PrivilegeCheckerTest {

    @Test
    void testAccessRightChecker() throws PMException {
        TestPAP pap = new TestPAP();

        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet(
            AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE));

        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.check(NodeUserContext.of(u1), id("o1"), List.of("read"));
        pap.check(NodeUserContext.of(u1), id("pc1"), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE);
        assertThrows(UnauthorizedException.class, () -> pap.check(NodeUserContext.of(u1), id("pc1"),
            AdminAccessRight.ADMIN_GRAPH_NODE_CREATE));
    }

}
