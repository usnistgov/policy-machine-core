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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EventContextParamsTest {

    @Test
    void testOk() throws PMException {
        String pml = """
            @EventCtx(a, b, string c)
            resourceop op1(string a, string b)
            @EventCtx(a, string c)
            resourceop op2(string a, string b)
            @EventCtx(a)
            resourceop op3(string a, string b)
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
            @EventCtx(a, b, c)
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
            @EventCtx(a, string b)
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
