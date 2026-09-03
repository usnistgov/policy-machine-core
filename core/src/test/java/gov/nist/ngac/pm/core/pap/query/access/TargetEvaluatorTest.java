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

package gov.nist.ngac.pm.core.pap.query.access;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.HashSet;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TargetEvaluatorTest {

    @Test
    void testEvaluateWithAdjacentDescendantOfPCOnly() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            
            create u "u1" in ["ua1"]
            
            associate "ua1" to PM_ADMIN_POLICY_CLASSES with ["admin:graph:assignment:descendant:create"]
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(-1), pml);

        TargetEvaluator targetEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create")),
                new HashSet<>()
            ),
            NodeTargetContext.of(id("oa1"))
        );
        assertEquals(
            Map.of(id("pc1"), new AccessRightSet("admin:graph:assignment:descendant:create")),
            result.pcMap()
        );
    }

    @Test
    void testEvaluateWithAdjacentDescendantOfPCOnlyWithAssociationToAdminBase() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            
            create u "u1" in ["ua1"]
            
            associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:descendant:create"]
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(-1), pml);

        TargetEvaluator targetEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create")),
                new HashSet<>()
            ),
            NodeTargetContext.of(id("oa1"))
        );
        assertEquals(
            Map.of(id("pc1"), new AccessRightSet("admin:graph:assignment:descendant:create")),
            result.pcMap()
        );
    }

    @Test
    void testEvaluateWithAdjacentDescendantOfPCWithOtherAssignments() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            
            create oa "oa2" in ["pc1"]
            assign "oa1" to ["oa2"]
            create oa "oa3" in ["oa2"]
            
            create u "u1" in ["ua1"]
            
            associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:descendant:create"]
            associate "ua1" to "oa2" with ["admin:graph:assignment:ascendant:create"]
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(-1), pml);

        TargetEvaluator targetEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(
                    AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create"),
                    id("oa2"), new AccessRightSet("admin:graph:assignment:ascendant:create")),
                new HashSet<>()
            ),
            NodeTargetContext.of(id("oa1"))
        );
        assertEquals(
            Map.of(id("pc1"), new AccessRightSet("admin:graph:assignment:descendant:create", "admin:graph:assignment:ascendant:create")),
            result.pcMap()
        );

        result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(
                    AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create"),
                    id("oa2"), new AccessRightSet("admin:graph:assignment:ascendant:create")),
                new HashSet<>()
            ),
            NodeTargetContext.of(id("oa3"))
        );
        assertEquals(
            Map.of(id("pc1"), new AccessRightSet("admin:graph:assignment:ascendant:create")),
            result.pcMap()
        );
    }
}