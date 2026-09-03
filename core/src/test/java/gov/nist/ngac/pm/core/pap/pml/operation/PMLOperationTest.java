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

package gov.nist.ngac.pm.core.pap.pml.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public class PMLOperationTest {

    @Test
    void testOperationsPersisted() throws PMException {
        String pml = """
            function test() string {
                return "test"
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.executePML(NodeUserContext.of(0), pml);

        assertTrue(memoryPAP.query().operations().getOperationNames().contains("test"));

        pml = """
            create PC test()
            """;
        memoryPAP.executePML(NodeUserContext.of(0), pml);

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

    @Test
    void testFunctionsCannotCallOperationsOrRoutines() throws PMException {
        String pml = """
            adminop op1(string s) {
                create pc s
            }
            
            function test() {
               op1(d="test")
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
            () -> memoryPAP.executePML(NodeUserContext.of(0), pml));
        assertEquals("unknown operation 'op1' in scope",
            e.getErrors().getFirst().errorMessage());

        String pml2 = """
            routine rou1(string s) {
                create pc s
            }
            
            function test() {
               rou1(a="test")
            }
            """;
        MemoryPAP memoryPAP2 = new MemoryPAP();
        e = assertThrows(PMLCompilationException.class,
            () -> memoryPAP2.executePML(NodeUserContext.of(0), pml2));
        assertEquals("unknown operation 'rou1' in scope",
            e.getErrors().getFirst().errorMessage());
    }

}
