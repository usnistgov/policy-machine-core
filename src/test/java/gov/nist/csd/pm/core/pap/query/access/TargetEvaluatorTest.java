package gov.nist.csd.pm.core.pap.query.access;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
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
            
            associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["admin:graph:assignment:descendant:create"]
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new UserContext(-1), pml);

        TargetEvaluator targetEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create")),
                new HashSet<>()
            ),
            new TargetContext(id("oa1"))
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
            
            associate "ua1" and PM_ADMIN_BASE_OA with ["admin:graph:assignment:descendant:create"]
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new UserContext(-1), pml);

        TargetEvaluator targetEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create")),
                new HashSet<>()
            ),
            new TargetContext(id("oa1"))
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
            
            associate "ua1" and PM_ADMIN_BASE_OA with ["admin:graph:assignment:descendant:create"]
            associate "ua1" and "oa2" with ["admin:graph:assignment:ascendant:create"]
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new UserContext(-1), pml);

        TargetEvaluator targetEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult result = targetEvaluator.evaluate(
            new UserDagResult(
                Map.of(
                    AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create"),
                    id("oa2"), new AccessRightSet("admin:graph:assignment:ascendant:create")),
                new HashSet<>()
            ),
            new TargetContext(id("oa1"))
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
            new TargetContext(id("oa3"))
        );
        assertEquals(
            Map.of(id("pc1"), new AccessRightSet("admin:graph:assignment:ascendant:create")),
            result.pcMap()
        );
    }
}