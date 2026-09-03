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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CachedTargetEvaluatorTest {

    private MemoryPAP pap;
    private UserDagResult userDagResult;
    private UserDagResult alternateUserDagResult;

    @BeforeEach
    void setUp() throws PMException {
        String pml = """
            set resource access rights ["read", "write"]

            create pc "pc1"
            create pc "pc2"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create oa "oa2" in ["pc1"]
            create oa "oa3" in ["pc2"]
            create o "o1" in ["oa1"]
            create o "o2" in ["oa2"]
            create o "o3" in ["oa3"]

            assign "oa2" to ["oa1"]
            assign "o2" to ["oa1"]

            create u "u1" in ["ua1"]

            associate "ua1" to "oa1" with ["read", "write"]
            associate "ua1" to "oa2" with ["read"]
            associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:descendant:create"]
            """;
        pap = new TestPAP();
        pap.executePML(NodeUserContext.of(-1), pml);

        userDagResult = new UserDagResult(
            Map.of(
                id("oa1"), new AccessRightSet("read", "write"),
                id("oa2"), new AccessRightSet("read"),
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("assign_to")
            ),
            Set.of(
                new NodeProhibition(
                    "p1",
                    id("u1"),
                    new AccessRightSet("write"),
                    Set.of(id("oa2")),
                    Set.of(),
                    false
                )
            )
        );

        alternateUserDagResult = new UserDagResult(
            Map.of(
                id("oa1"), new AccessRightSet("read"),
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("assign_to")
            ),
            Set.of()
        );
    }

    @Test
    void testAccuracyAgainstRegularTargetEvaluator() throws PMException {
        TargetEvaluator regularEvaluator = new TargetEvaluator(pap.policyStore());
        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());

        // Test multiple target contexts
        TargetContext[] contexts = {
            NodeTargetContext.of(id("o1")),
            NodeTargetContext.of(id("o2")),
            NodeTargetContext.of(id("oa1")),
            NodeTargetContext.of(id("oa2"))
        };

        for (TargetContext context : contexts) {
            TargetDagResult regularResult = regularEvaluator.evaluate(userDagResult, context);
            TargetDagResult cachedResult = cachedEvaluator.evaluate(userDagResult, context);

            assertEquals(regularResult.pcMap(), cachedResult.pcMap());
            assertEquals(regularResult.reachedTargets(), cachedResult.reachedTargets());
        }
    }

    @Test
    void testConsistentResults() throws PMException {
        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());

        TargetDagResult result1 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));
        TargetDagResult result2 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));
        TargetDagResult result3 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));

        assertEquals(result1.pcMap(), result2.pcMap());
        assertEquals(result2.pcMap(), result3.pcMap());
        assertEquals(result1.reachedTargets(), result2.reachedTargets());
        assertEquals(result2.reachedTargets(), result3.reachedTargets());
    }

    @Test
    void testCacheInvalidatedOnUserDagResultChange() throws PMException {
        TargetEvaluator regularEvaluator = new TargetEvaluator(pap.policyStore());
        TargetDagResult regularResult1 = regularEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));
        TargetDagResult regularResult2 = regularEvaluator.evaluate(alternateUserDagResult, NodeTargetContext.of(id("o1")));

        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());

        TargetDagResult result1 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));

        TargetDagResult result2 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));
        assertEquals(result1.pcMap(), result2.pcMap());

        TargetDagResult result3 = cachedEvaluator.evaluate(alternateUserDagResult, NodeTargetContext.of(id("o1")));
        assertEquals(regularResult1.pcMap(), result1.pcMap());
        assertEquals(regularResult2.pcMap(), result3.pcMap());

        TargetDagResult result4 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));
        assertEquals(result1.pcMap(), result4.pcMap());
    }

    @Test
    void testMultipleTargetContextsWithSameUserDagResult() throws PMException {
        TargetEvaluator regularEvaluator = new TargetEvaluator(pap.policyStore());
        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());

        TargetContext[] contexts = {
            NodeTargetContext.of(id("o1")),
            NodeTargetContext.of(id("o2")),
            NodeTargetContext.of(id("oa1")),
            NodeTargetContext.of(id("oa2")),
            NodeTargetContext.of(id("o3"))
        };

        for (TargetContext context : contexts) {
            TargetDagResult regularResult = regularEvaluator.evaluate(userDagResult, context);
            TargetDagResult cachedResult = cachedEvaluator.evaluate(userDagResult, context);

            assertEquals(regularResult.pcMap(), cachedResult.pcMap());
            assertEquals(regularResult.reachedTargets(), cachedResult.reachedTargets());
        }
    }

    @Test
    void testCacheDoesNotAffectProhibitionResolution() throws PMException {
        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());

        TargetDagResult result1 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o2")));
        // evaluating a new target should reset the prohibition evaluation state
        TargetDagResult result2 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o1")));
        TargetDagResult result3 = cachedEvaluator.evaluate(userDagResult, NodeTargetContext.of(id("o2")));

        assertEquals(result1, result3);
        assertNotEquals(result1, result2);
    }

    @Test
    void testEmptyUserDagResult() throws PMException {
        UserDagResult emptyUserDagResult = new UserDagResult(Map.of(), Set.of());
        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());

        TargetDagResult result = cachedEvaluator.evaluate(emptyUserDagResult, NodeTargetContext.of(id("o1")));

        assertNotNull(result);
        assertTrue(result.pcMap().isEmpty() || result.pcMap().values().stream().allMatch(AccessRightSet::isEmpty));
    }

    @Test
    void testCacheWithUserDagResultDifferences() throws PMException {
        UserDagResult userWithWriteAccess = new UserDagResult(
            Map.of(
                id("oa1"), new AccessRightSet("read", "write"),
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("admin:graph:assignment:descendant:create")
            ),
            Set.of()
        );

        UserDagResult userWithNoAccess = new UserDagResult(
            Map.of(),
            Set.of()
        );

        CachedTargetEvaluator cachedEvaluator = new CachedTargetEvaluator(pap.policyStore());
        TargetEvaluator regularEvaluator = new TargetEvaluator(pap.policyStore());

        TargetDagResult cachedResultWithAccess = cachedEvaluator.evaluate(userWithWriteAccess, NodeTargetContext.of(id("o1")));
        TargetDagResult regularResultWithAccess = regularEvaluator.evaluate(userWithWriteAccess, NodeTargetContext.of(id("o1")));

        TargetDagResult cachedResultNoAccess = cachedEvaluator.evaluate(userWithNoAccess, NodeTargetContext.of(id("o1")));
        TargetDagResult regularResultNoAccess = regularEvaluator.evaluate(userWithNoAccess, NodeTargetContext.of(id("o1")));

        assertEquals(regularResultWithAccess.pcMap(), cachedResultWithAccess.pcMap());
        assertEquals(regularResultNoAccess.pcMap(), cachedResultNoAccess.pcMap());

        TargetDagResult cachedResultWithAccessAgain = cachedEvaluator.evaluate(userWithWriteAccess, NodeTargetContext.of(id("o1")));
        assertEquals(cachedResultWithAccess.pcMap(), cachedResultWithAccessAgain.pcMap());
    }
}
