package gov.nist.csd.pm.core.pap.pml.pattern;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.obligation.event.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.NegateSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ParenSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ProcessSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.UsernamePatternExpression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SubjectPatternTest {

    @Test
    void testSubjectPattern() throws PMException {
        MemoryPAP pap = new TestPAP();
        SubjectPattern pattern = new SubjectPattern();
        assertTrue(pattern.matches(new EventContextUser("test"), pap.query()));

        pattern = new SubjectPattern(new UsernamePatternExpression("test"));
        assertTrue(pattern.matches(new EventContextUser("test"), pap.query()));
        assertFalse(pattern.matches(new EventContextUser("test1"), pap.query()));
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
                create obligation "ob1"
                when any user
                performs any operation
                do(ctx) { }
                """;
        CreateObligationStatement stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), pap.query()));

        pml = """
                create obligation "ob1" 
                    when user "u1"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new UsernamePatternExpression("u1")), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), pap.query()));

        pml = """
                create obligation "ob1"
                    when user "u1" || "u2"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression("u1"),
                new UsernamePatternExpression("u2"),
                false
        )), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), pap.query()));

        pml = """
                create obligation "ob1"
                    when user "u1" && in "ua2"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression("u1"),
                new InSubjectPatternExpression("ua2"),
                true
        )), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), pap.query()));
        assertFalse(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u2"), pap.query()));

        pml = """
                create obligation "ob1"
                    when user !in "ua1"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new NegateSubjectPatternExpression(
                new InSubjectPatternExpression("ua1")
        )), stmt.getEventPattern().getSubjectPattern());
        assertFalse(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), pap.query()));
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u2"), pap.query()));

        pml = """
                create obligation "ob1"
                    when user ("u1" && in "ua2") || "u2"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
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
        )), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), pap.query()));
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u2"), pap.query()));

        pml = """
                create obligation "ob1"
                    when user process "p1"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new ProcessSubjectPatternExpression("p1")), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1", "p1"), pap.query()));
        assertFalse(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1", "p2"), pap.query()));
    }

    @Test
    void testInPatternWhenUserDoesNotMatch() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua3" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1", "ua3"]
                create u "u2" in ["ua2"]
                
                associate "ua1" to "ua2" with ["*"]
                associate "ua1" to "ua3" with ["*"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["*"]
                
                create obligation "ob1"
                    when user in "ua2"
                    performs any operation
                    do(ctx) {
                        create pc "test"
                    }
                """;
        MemoryPAP memoryPAP = new TestPAP();
        memoryPAP.executePML(new UserContext(id("u1")), pml);

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