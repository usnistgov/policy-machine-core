package gov.nist.csd.pm.core.pap.pml.pattern.arg;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateRuleStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.pml.pattern.PatternTestUtil.compileTestCreateRuleStatement;
import static org.junit.jupiter.api.Assertions.*;

public class ArgPatternTest {

    @Test
    void testArgPattern() throws PMException {
        MemoryPAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        pap.modify().graph().createUser("u2", List.of(ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        pap.modify().graph().createObject("o1", List.of(oa1));
        pap.modify().graph().createObject("o2", List.of(oa2));

        String pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs "assign"
                    on {
                        ascendant: "o1",
                        descendants: "oa1"
                    }
                    do(ctx) { }
                }
                """;
        CreateRuleStatement stmt = compileTestCreateRuleStatement(pml);
        assertEquals(
                Map.of(
                        "ascendant", List.of(new NodeNameArgPatternExpression("o1")),
                        "descendants", List.of(new NodeNameArgPatternExpression("oa1"))
                ),
                stmt.getArgPattern()
        );
        assertTrue(stmt.getArgPattern().get("ascendant").getFirst().matches("o1", pap));
        assertTrue(stmt.getArgPattern().get("descendants").getFirst().matches("oa1", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs "assign"
                    on {
                        ascendant: ["o1"],
                        descendants: ["oa1"]
                    }
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(
                Map.of(
                        "ascendant", List.of(new NodeNameArgPatternExpression("o1")),
                        "descendants", List.of(new NodeNameArgPatternExpression("oa1"))
                ),
                stmt.getArgPattern()
        );
        assertTrue(stmt.getArgPattern().get("ascendant").getFirst().matches("o1", pap));
        assertTrue(stmt.getArgPattern().get("descendants").getFirst().matches("oa1", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs "assign"
                    on {
                        ascendant: ["o1", "o2"],
                        descendants: [!"oa1"]
                    }
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(
                Map.of(
                        "ascendant", List.of(new NodeNameArgPatternExpression("o1"), new NodeNameArgPatternExpression("o2")),
                        "descendants", List.of(new NegateArgPatternExpression(new NodeNameArgPatternExpression("oa1")))
                ),
                stmt.getArgPattern()
        );
        assertTrue(stmt.getArgPattern().get("ascendant").getFirst().matches("o1", pap));
        assertFalse(stmt.getArgPattern().get("ascendant").getFirst().matches("o2", pap));
        assertTrue(stmt.getArgPattern().get("ascendant").get(1).matches("o2", pap));
        assertFalse(stmt.getArgPattern().get("ascendant").get(1).matches("o3", pap));
        assertFalse(stmt.getArgPattern().get("descendants").getFirst().matches("oa1", pap));
        assertTrue(stmt.getArgPattern().get("descendants").getFirst().matches("oa2", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs "assign"
                    on {
                        ascendant: ["o1", any],
                        descendants: [!"oa1", ("oa1" || "oa2")]
                    }
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(
                Map.of(
                        "ascendant", List.of(new NodeNameArgPatternExpression("o1"), new AnyArgPatternExpression()),
                        "descendants", List.of(
                                new NegateArgPatternExpression(new NodeNameArgPatternExpression("oa1")),
                                new ParenArgPatternExpression(
                                        new LogicalArgPatternExpression(
                                                new NodeNameArgPatternExpression("oa1"),
                                                new NodeNameArgPatternExpression("oa2"),
                                                false
                                        )
                                )
                        )
                ),
                stmt.getArgPattern()
        );
        assertTrue(stmt.getArgPattern().get("ascendant").getFirst().matches("o1", pap));
        assertFalse(stmt.getArgPattern().get("ascendant").getFirst().matches("o2", pap));
        assertTrue(stmt.getArgPattern().get("ascendant").get(1).matches("o2", pap));
        assertTrue(stmt.getArgPattern().get("ascendant").get(1).matches("o3", pap));
        assertFalse(stmt.getArgPattern().get("descendants").getFirst().matches("oa1", pap));
        assertTrue(stmt.getArgPattern().get("descendants").get(1).matches("oa1", pap));
        assertTrue(stmt.getArgPattern().get("descendants").get(1).matches("oa2", pap));
    }

    @Test
    void testOnlyNodeOpsInEventContext() throws PMException {
        MemoryPAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        assertThrows(NodeDoesNotExistException.class, () -> pap.executePML(new UserContext(u1), """
                associate "ua1" and PM_ADMIN_BASE_OA with ["*a"]
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs "create_object_attribute"
                    on {
                        name: "oa2",
                        descendants: any
                    }
                    do(ctx) {
                        create oa "oa2" in ["oa1"]
                    }
                }
                """));
    }

}
