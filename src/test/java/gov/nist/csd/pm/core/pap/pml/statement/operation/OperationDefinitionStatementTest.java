package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationDefinitionStatementTest {

    @Test
    void testWithChecks() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create ua "ua2" in ["pc1"]
                create u "u2" in ["ua2"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["assign"]
                
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                create o "o3" in ["oa1"]
                
                operation op1(string a, []string b) {
                    check "assign" on [a]
                    check "assign" on b
                    check "assign" on ["oa1"]
                } {
                    create PC "test"
                }
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(new TestUserContext("u1"), tx -> {
            tx.executePML("""
                op1("o1", ["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> pdp.runTx(new UserContext(id("u2")), tx -> {
            tx.executePML("""
                op1("o1", ["o2", "o3"])
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
                associate "ua1" and "oa1" with ["assign"]
                
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                create o "o3" in ["oa1"]
                
                operation op1(string a, []string b) {
                    create PC a
                }
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(new TestUserContext("u1"), tx -> {
            tx.executePML("""
                op1("test1", ["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test1"));

        pdp.runTx(new UserContext(id("u2")), tx -> {
            tx.executePML("""
                op1("test2", ["o2", "o3"])
                """);
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

}