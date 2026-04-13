package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.query.model.context.IdUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.NameUserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

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
                
                @reqcap({
                    require ["admin:graph:assignment:ascendant:create"] on [a]
                    require ["admin:graph:assignment:ascendant:create"] on b
                    require ["admin:graph:assignment:ascendant:create"] on ["oa1"]
                })
                adminop op1(string a, []string b) {
                    create PC "test"
                }
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new NameUserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(new NameUserContext("u1"), tx -> {
            tx.executePML("""
                op1(a="o1", b=["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> pdp.runTx(new IdUserContext(id("u2")), tx -> {
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
        pap.executePML(new NameUserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(new NameUserContext("u1"), tx -> {
            tx.executePML("""
                op1(a="test1", b=["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test1"));

        pdp.runTx(new IdUserContext(id("u2")), tx -> {
            tx.executePML("""
                op1(a="test2", b=["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

}