package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRuleStatement;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.pml.pattern.PatternTestUtil.compileTestCreateRuleStatement;
import static org.junit.jupiter.api.Assertions.*;

public class OperandPatternTest {

    @Test
    void testOperandPattern() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1", "ua2"));
        pap.modify().graph().createUser("u2", List.of("ua2"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
        pap.modify().graph().createObject("o1", List.of("oa1"));
        pap.modify().graph().createObject("o2", List.of("oa2"));

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
                        "ascendant", List.of(new NodeOperandPattern("o1")),
                        "descendants", List.of(new NodeOperandPattern("oa1"))
                ),
                stmt.getOperandPattern()
        );
        assertTrue(stmt.getOperandPattern().get("ascendant").getFirst().matches("o1", pap));
        assertTrue(stmt.getOperandPattern().get("descendants").getFirst().matches("oa1", pap));

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
                        "ascendant", List.of(new NodeOperandPattern("o1")),
                        "descendants", List.of(new NodeOperandPattern("oa1"))
                ),
                stmt.getOperandPattern()
        );
        assertTrue(stmt.getOperandPattern().get("ascendant").getFirst().matches("o1", pap));
        assertTrue(stmt.getOperandPattern().get("descendants").getFirst().matches("oa1", pap));

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
                        "ascendant", List.of(new NodeOperandPattern("o1"), new NodeOperandPattern("o2")),
                        "descendants", List.of(new NegateOperandPatternExpression(new NodeOperandPattern("oa1")))
                ),
                stmt.getOperandPattern()
        );
        assertTrue(stmt.getOperandPattern().get("ascendant").getFirst().matches("o1", pap));
        assertFalse(stmt.getOperandPattern().get("ascendant").getFirst().matches("o2", pap));
        assertTrue(stmt.getOperandPattern().get("ascendant").get(1).matches("o2", pap));
        assertFalse(stmt.getOperandPattern().get("ascendant").get(1).matches("o3", pap));
        assertFalse(stmt.getOperandPattern().get("descendants").getFirst().matches("oa1", pap));
        assertTrue(stmt.getOperandPattern().get("descendants").getFirst().matches("oa2", pap));

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
                        "ascendant", List.of(new NodeOperandPattern("o1"), new AnyOperandPattern()),
                        "descendants", List.of(
                                new NegateOperandPatternExpression(new NodeOperandPattern("oa1")),
                                new ParenOperandPatternExpression(
                                        new LogicalOperandPatternExpression(
                                                new NodeOperandPattern("oa1"),
                                                new NodeOperandPattern("oa2"),
                                                false
                                        )
                                )
                        )
                ),
                stmt.getOperandPattern()
        );
        assertTrue(stmt.getOperandPattern().get("ascendant").getFirst().matches("o1", pap));
        assertFalse(stmt.getOperandPattern().get("ascendant").getFirst().matches("o2", pap));
        assertTrue(stmt.getOperandPattern().get("ascendant").get(1).matches("o2", pap));
        assertTrue(stmt.getOperandPattern().get("ascendant").get(1).matches("o3", pap));
        assertFalse(stmt.getOperandPattern().get("descendants").getFirst().matches("oa1", pap));
        assertTrue(stmt.getOperandPattern().get("descendants").get(1).matches("oa1", pap));
        assertTrue(stmt.getOperandPattern().get("descendants").get(1).matches("oa2", pap));
    }

    @Test
    void testOnlyNodeOpsInEventContext() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        assertThrows(NodeDoesNotExistException.class, () -> pap.executePML(new UserContext("u1"), """
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*a"]
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
