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

package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class AdminOpDefinitionStatementTest {

    @Test
    void testWithChecks() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create ua "ua2" in ["pc1"]
                create u "u2" in ["ua2"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["admin:graph:assignment:ascendant:create"]
                
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                create o "o3" in ["oa1"]
                
                @ReqCap({
                    require ["admin:graph:assignment:ascendant:create"] on [a]
                    require ["admin:graph:assignment:ascendant:create"] on b
                    require ["admin:graph:assignment:ascendant:create"] on ["oa1"]
                })
                adminop op1(string a, []string b) {
                    create PC "test"
                }
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(NodeUserContext.of("u1"), tx -> {
            tx.executePML("""
                op1(a="o1", b=["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> pdp.runTx(NodeUserContext.of(id("u2")), tx -> {
            tx.executePML("""
                op1(a="o1", b=["o2", "o3"])
                """);
            return null;
        }));
    }

    @Test
    void testWithNoChecks() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create ua "ua2" in ["pc1"]
                create u "u2" in ["ua2"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["admin:graph:assignment:ascendant:create"]
                
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                create o "o3" in ["oa1"]
                
                adminop op1(string a, []string b) {
                    create PC a
                }
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(NodeUserContext.of("u1"), tx -> {
            tx.executePML("""
                op1(a="test1", b=["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test1"));

        pdp.runTx(NodeUserContext.of(id("u2")), tx -> {
            tx.executePML("""
                op1(a="test2", b=["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

}