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

package gov.nist.ngac.pm.core.pap.operation.reqcap;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class RequiredCapabilityTest {

    @Test
    void testIsSatisfiedWithEmptyPrivileges() throws PMException {
        MemoryPAP pap = new TestPAP();
        RequiredCapability reqCap = new RequiredCapability(List.of());

        assertTrue(reqCap.isSatisfied(pap, NodeUserContext.of(id("u1")), new Args()));
    }

    @Test
    void testIsSatisfiedWhenAllPrivilegesMet() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:node:create", "admin:graph:node:delete"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(NodeUserContext.of(id("u1")), pml);

        RequiredCapability reqCap = new RequiredCapability(
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
            ),
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_DELETE)
            )
        );

        assertTrue(reqCap.isSatisfied(pap, NodeUserContext.of(id("u1")), new Args()));
    }

    @Test
    void testIsSatisfiedReturnsFalseWhenOnePrivilegeFails() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:node:create"]
                create u "u1" in ["ua1"]
                """;

        pap.executePML(NodeUserContext.of(id("u1")), pml);

        RequiredCapability reqCap = new RequiredCapability(
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
            ),
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_DELETE)
            )
        );

        assertFalse(reqCap.isSatisfied(pap, NodeUserContext.of(id("u1")), new Args()));
    }
}
