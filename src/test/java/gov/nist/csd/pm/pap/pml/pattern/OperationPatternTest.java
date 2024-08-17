package gov.nist.csd.pm.pap.pml.pattern;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRuleStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.pattern.PatternTestUtil.compileTestCreateRuleStatement;
import static org.junit.jupiter.api.Assertions.*;

class OperationPatternTest {

    @Test
    void testOperationPattern() throws PMException {
        OperationPattern pattern = new OperationPattern();
        assertTrue(pattern.matches("test", new MemoryPAP()));

        pattern = new OperationPattern("test");
        assertTrue(pattern.matches("test", new MemoryPAP()));
        assertFalse(pattern.matches("test1", new MemoryPAP()));
    }

    @Test
    void testPML() throws PMException {
        String pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) { }
                }
                """;
        CreateRuleStatement stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new OperationPattern(), stmt.getOperationPattern());

        pml = """
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs "op1"
                    do(ctx) { }
                }
                """;
        stmt = compileTestCreateRuleStatement(pml);
        assertEquals(new OperationPattern("op1"), stmt.getOperationPattern());
    }

}