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

package gov.nist.ngac.pm.core.pap.pml.pattern;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.epp.EPP;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.InSubjectPatternExpression;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.LogicalSubjectPatternExpression;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.NegateSubjectPatternExpression;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.ParenSubjectPatternExpression;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.ProcessSubjectPatternExpression;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.UsernamePatternExpression;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class SubjectPatternTest {

    @Test
    void testSubjectPattern() throws PMException {
        MemoryPAP pap = new TestPAP();
        SubjectPattern pattern = new SubjectPattern();
        ExecutionContext executionContext = pap.buildExecutionContext(null);
        assertTrue(pattern.matches(new EventContextUser("test"), executionContext, pap));

        pattern = new SubjectPattern(new UsernamePatternExpression(new StringLiteralExpression("test")));
        assertTrue(pattern.matches(new EventContextUser("test"), executionContext, pap));
        assertFalse(pattern.matches(new EventContextUser("test1"), executionContext, pap));
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
        ExecutionContext executionContext = pap.buildExecutionContext(null);
        CreateObligationStatement stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), executionContext, pap));

        pml = """
                create obligation "ob1" 
                    when user "u1"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new UsernamePatternExpression(new StringLiteralExpression("u1"))), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), executionContext, pap));

        pml = """
                create obligation "ob1"
                    when user "u1" || user "u2"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression(new StringLiteralExpression("u1")),
                new UsernamePatternExpression(new StringLiteralExpression("u2")),
                false
        )), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), executionContext, pap));

        pml = """
                create obligation "ob1"
                    when user "u1" && user in "ua2"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression(new StringLiteralExpression("u1")),
                new InSubjectPatternExpression(new StringLiteralExpression("ua2")),
                true
        )), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), executionContext, pap));
        assertFalse(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u2"), executionContext, pap));

        pml = """
                create obligation "ob1"
                    when !user in "ua1"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new NegateSubjectPatternExpression(
                new InSubjectPatternExpression(new StringLiteralExpression("ua1"))
        )), stmt.getEventPattern().getSubjectPattern());
        assertFalse(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), executionContext, pap));
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u2"), executionContext, pap));

        pml = """
                create obligation "ob1"
                    when (user "u1" && user in "ua2") || user "u2"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new LogicalSubjectPatternExpression(
                new ParenSubjectPatternExpression(
                        new LogicalSubjectPatternExpression(
                                new UsernamePatternExpression(new StringLiteralExpression("u1")),
                                new InSubjectPatternExpression(new StringLiteralExpression("ua2")),
                                true
                        )
                ),
                new UsernamePatternExpression(new StringLiteralExpression("u2")),
                false
        )), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1"), executionContext, pap));
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u2"), executionContext, pap));

        pml = """
                create obligation "ob1"
                    when process "p1"
                    performs any operation
                    do(ctx) { }
                """;
        stmt = PatternTestUtil.compileTestCreateObligationStatement(pap, pml);
        assertEquals(new SubjectPattern(new ProcessSubjectPatternExpression(new StringLiteralExpression("p1"))), stmt.getEventPattern().getSubjectPattern());
        assertTrue(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1", "p1"), executionContext, pap));
        assertFalse(stmt.getEventPattern().getSubjectPattern().matches(new EventContextUser("u1", "p2"), executionContext, pap));
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
        memoryPAP.executePML(NodeUserContext.of(id("u1")), pml);

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