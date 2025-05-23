package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.AnyArgPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.NodeArgPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.ProcessSubjectPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.UsernamePattern;
import gov.nist.csd.pm.core.util.TestPAP;
import it.unimi.dsi.fastutil.longs.LongList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.ASCENDANT_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.DESCENDANTS_PARAM;
import static org.junit.jupiter.api.Assertions.*;

class EventPatternTest {

    private MemoryPAP testPAP() throws PMException {
        MemoryPAP pap = new TestPAP();

        GraphModification graph = pap.modify().graph();

        long pc1 = graph.createPolicyClass("pc1");
        long ua1 = graph.createUserAttribute("ua1", LongList.of(pc1));
        graph.createUser("u1", LongList.of(ua1));

        return pap;
    }

    @Test
    void testOperationMatches() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(),
                new OperationPattern(),
                Map.of()
        );

        PAP pap = testPAP();

        EventContext eventContext = new EventContext(
                "u1",
                null,
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

        assertTrue(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testOperationDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(),
                new OperationPattern("op1"),
                Map.of()
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                null,
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testUserDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u2")),
                new OperationPattern(),
                Map.of()
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

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

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "p1",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

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

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "p2",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

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

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "p2",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testArgsMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u1")),
                new OperationPattern("assign"),
                Map.of(
                        "ascendant", List.of(new NodeArgPattern("a")),
                        "descendants", List.of(new AnyArgPattern())
                )
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

        assertTrue(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testArgsDoNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u1")),
                new OperationPattern("assign"),
                Map.of(
                        "ascendant", List.of(new NodeArgPattern("b")),
                        "descendant", List.of(new AnyArgPattern())
                )
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), List.of("b"))
        );

        assertFalse(eventPattern.matches(eventContext, pap));
    }

    @Test
    void testInvalidNodeArgType() throws PMException {
        EventPattern eventPattern = new EventPattern(
                new SubjectPattern(new UsernamePattern("u1")),
                new OperationPattern("assign"),
                Map.of(
                        "ascendant", List.of(new AnyArgPattern()),
                        "descendants", List.of(new AnyArgPattern())
                )
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
                "u1",
                "",
                "assign",
                Map.of(ASCENDANT_PARAM.getName(), "a", DESCENDANTS_PARAM.getName(), Map.of("b", ""))
        );

        assertThrows(UnexpectedArgTypeException.class,
                () -> eventPattern.matches(eventContext, pap));
    }

}