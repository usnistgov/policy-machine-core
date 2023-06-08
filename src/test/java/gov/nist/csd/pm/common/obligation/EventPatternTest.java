package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.op.graph.AssignOp;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.AnyOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.NodeOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.subject.ProcessSubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.UsernamePattern;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.graph.GraphOp.ASCENDANT_OPERAND;

import static gov.nist.csd.pm.pap.op.graph.GraphOp.DESCENDANTS_OPERAND;
import static org.junit.jupiter.api.Assertions.*;

class EventPatternTest {

    @Test
    void testOperationMatches() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(),
                new OperationPattern(),
                Map.of()
        );

        EventContext eventContext = new EventContext(
                "u1",
                null,
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertTrue(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testOperationDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(),
                new OperationPattern("op1"),
                Map.of()
        );

        EventContext eventContext = new EventContext(
                "u1",
                null,
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testUserDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u2")),
                new OperationPattern(),
                Map.of()
        );

        EventContext eventContext = new EventContext(
                "u1",
                "",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testUserAndProcessMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new LogicalSubjectPatternExpression(
                        new UsernamePattern("u1"),
                        new ProcessSubjectPattern("p1"),
                        false
                )),
                new OperationPattern(),
                Map.of()
        );

        EventContext eventContext = new EventContext(
                "u1",
                "p1",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertTrue(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testUserMatchesProcessDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new LogicalSubjectPatternExpression(
                        new UsernamePattern("u1"),
                        new ProcessSubjectPattern("p1"),
                        false
                )),
                new OperationPattern(),
                Map.of()
        );

        EventContext eventContext = new EventContext(
                "u1",
                "p2",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertTrue(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testUserAndProcessDoNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new LogicalSubjectPatternExpression(
                        new UsernamePattern("u2"),
                        new ProcessSubjectPattern("p1"),
                        false
                )),
                new OperationPattern(),
                Map.of()
        );

        EventContext eventContext = new EventContext(
                "u1",
                "p2",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testOperandsMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u1")),
                new OperationPattern("assign"),
                Map.of(
                        "ascendant", List.of(new NodeOperandPattern("a")),
                        "descendant", List.of(new AnyOperandPattern())
                )
        );

        EventContext eventContext = new EventContext(
                "u1",
                "",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertTrue(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testOperandsDoNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u1")),
                new OperationPattern("assign"),
                Map.of(
                        "ascendant", List.of(new NodeOperandPattern("b")),
                        "descendant", List.of(new AnyOperandPattern())
                )
        );

        EventContext eventContext = new EventContext(
                "u1",
                "",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))
        );

        MemoryPAP pap = new MemoryPAP();

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testInvalidNodeOperandType() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u1")),
                new OperationPattern("assign"),
                Map.of(
                        "ascendant", List.of(new AnyOperandPattern()),
                        "descendants", List.of(new AnyOperandPattern())
                )
        );

        EventContext eventContext = new EventContext(
                "u1",
                "",
                new AssignOp(),
                Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, Map.of("b", ""))
        );

        MemoryPAP pap = new MemoryPAP();

        assertThrows(UnexpectedOperandTypeException.class,
                () -> eventPattern.matches(eventContext, pap));
    }

}