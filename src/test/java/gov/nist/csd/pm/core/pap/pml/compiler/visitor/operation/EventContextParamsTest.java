package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EventContextParamsTest {

    @Test
    void testOk() throws PMException {
        String pml = """
            @eventctx(a, b, string c)
            resourceop op1(string a, string b)
            """;

        TestPAP testPAP = new TestPAP();
        testPAP.executePML(null, pml);

        Operation<?> operation = testPAP.query().operations().getOperation("op1");
        List<FormalParameter<?>> actual = operation.getEventParameters();
        assertEquals(3, actual.size());
        assertTrue(actual.contains(new FormalParameter<>("a", STRING_TYPE)));
        assertTrue(actual.contains(new FormalParameter<>("b", STRING_TYPE)));
        assertTrue(actual.contains(new FormalParameter<>("c", STRING_TYPE)));
    }

    @Test
    void test_whenEventParamDoesNotHaveTypeAndIsNotFormalParam_throwsException() throws PMException {
        String pml = """
            @eventctx(a, b, c)
            resourceop op1(string a, string b)
            """;

        TestPAP testPAP = new TestPAP();
        PMLCompilationException e = assertThrows(
            PMLCompilationException.class,
            () -> testPAP.executePML(null, pml)
        );
        assertEquals(
            "event arg 'c' has no type and does not match any formal parameter",
            e.getErrors().getFirst().errorMessage()
        );
    }

    @Test
    void test_whenEventParamDoesClashesWithFormalParam_throwsException() throws PMException {
        String pml = """
            @eventctx(a, string b)
            resourceop op1(string a, string b)
            """;

        TestPAP testPAP = new TestPAP();
        PMLCompilationException e = assertThrows(
            PMLCompilationException.class,
            () -> testPAP.executePML(null, pml)
        );
        assertEquals(
            "event arg already defined as formal parameter 'b'",
            e.getErrors().getFirst().errorMessage()
        );
    }
}
