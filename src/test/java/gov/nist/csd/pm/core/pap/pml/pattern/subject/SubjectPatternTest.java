package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateRuleStatement;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.bootstrap.PMLBootstrapper;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.Map;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.pap.pml.pattern.PatternTestUtil.compileTestCreateRuleStatement;
import static org.junit.jupiter.api.Assertions.*;

class SubjectPatternTest {

    @Test
    void testSubjectPattern() throws PMException {
        MemoryPAP pap = new TestPAP();
        SubjectPattern pattern = new SubjectPattern();
        assertTrue(pattern.matches(new EventContextUser("test"), pap));

        pattern = new SubjectPattern(new UsernamePatternExpression("test"));
        assertTrue(pattern.matches(new EventContextUser("test"), pap));
        assertFalse(pattern.matches(new EventContextUser("test1"), pap));
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
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u1"), pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user "u1"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new UsernamePatternExpression("u1")), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u1"), pap));

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
                new UsernamePatternExpression("u1"),
                new UsernamePatternExpression("u2"),
                false
        )), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u1"), pap));

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
                new UsernamePatternExpression("u1"),
                new InSubjectPatternExpression("ua2"),
                true
        )), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u1"), pap));
        assertFalse(stmt.getSubjectPattern().matches(new EventContextUser("u2"), pap));

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
                new InSubjectPatternExpression("ua1")
        )), stmt.getSubjectPattern());
        assertFalse(stmt.getSubjectPattern().matches(new EventContextUser("u1"), pap));
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u2"), pap));

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
                                new UsernamePatternExpression("u1"),
                                new InSubjectPatternExpression("ua2"),
                                true
                        )
                ),
                new UsernamePatternExpression("u2"),
                false
        )), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u1"), pap));
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u2"), pap));

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when user process "p1"
                    performs any operation
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new SubjectPattern(new ProcessSubjectPatternExpression("p1")), stmt.getSubjectPattern());
        assertTrue(stmt.getSubjectPattern().matches(new EventContextUser("u1", "p1"), pap));
        assertFalse(stmt.getSubjectPattern().matches(new EventContextUser("u1", "p2"), pap));
    }

    @Test
    void testInPatternWhenUserDoesNotMatch() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                assign "u2" to ["ua2"]
                
                create obligation "ob1" {
                    create rule "r1"
                    when user in "ua2"
                    performs any operation
                    do(ctx) {
                        create pc "test"
                    }
                }
                """;
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.bootstrap(new PMLBootstrapper(List.of(), List.of(), "u2", pml));

        PDP pdp = new PDP(memoryPAP);
        EPP epp = new EPP(pdp, memoryPAP);
        epp.processEvent(new EventContext(
            new EventContextUser("u1"),
            "test",
            Map.of()
        ));

        assertFalse(memoryPAP.query().graph().nodeExists("test"));

    }
}