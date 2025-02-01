package gov.nist.csd.pm.pap.pml.pattern.subject;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRuleStatement;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.pattern.PatternTestUtil.compileTestCreateRuleStatement;
import static org.junit.jupiter.api.Assertions.*;

class SubjectPatternTest {

    @Test
    void testSubjectPattern() throws PMException {
        MemoryPAP pap = new TestPAP();
        SubjectPattern pattern = new SubjectPattern();
        assertTrue(pattern.matches("test", pap));

        pattern = new SubjectPattern(new UsernamePattern("test"));
        assertTrue(pattern.matches("test", pap));
        assertFalse(pattern.matches("test1", pap));
    }

    @Test
    void testPML() throws PMException {
        MemoryPAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        pap.modify().graph().createUser("u2", List.of(ua2));

        String pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) { }
                }
                """;
        CreateRuleStatement stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches("u1", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new UsernamePattern("u1")), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches("u1", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1" || "u2"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePattern("u1"),
                new UsernamePattern("u2"),
                false
        )), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches("u1", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1" && in "ua2"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePattern("u1"),
                new InSubjectPattern("ua2"),
                true
        )), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches("u1", pap));
        assertFalse(stmt.getSubjectPattern().matches("u2", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user !in "ua1"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new NegateSubjectPatternExpression(
                new InSubjectPattern("ua1")
        )), stmt.getSubjectPattern());
        assertFalse(stmt.getSubjectPattern().matches("u1", pap));
        assertTrue(stmt.getSubjectPattern().matches("u2", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user ("u1" && in "ua2") || "u2"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new ParenSubjectPatternExpression(
                        new LogicalSubjectPatternExpression(
                                new UsernamePattern("u1"),
                                new InSubjectPattern("ua2"),
                                true
                        )
                ),
                new UsernamePattern("u2"),
                false
        )), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches("u1", pap));
        assertTrue(stmt.getSubjectPattern().matches("u2", pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user process "p1"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new ProcessSubjectPattern("p1")), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches("p1", pap));
        assertFalse(stmt.getSubjectPattern().matches("p2", pap));
    }
}