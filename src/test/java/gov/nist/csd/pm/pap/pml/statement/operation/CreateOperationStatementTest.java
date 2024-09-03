package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateOperationStatementTest {

    @Test
    void testWithChecks() throws PMException {
        String pml = "create pc \"pc1\"\n" +
                "                create ua \"ua1\" in [\"pc1\"]\n" +
                "                create u \"u1\" in [\"ua1\"]\n" +
                "                create ua \"ua2\" in [\"pc1\"]\n" +
                "                create u \"u2\" in [\"ua2\"]\n" +
                "                create oa \"oa1\" in [\"pc1\"]\n" +
                "                associate \"ua1\" and \"oa1\" with [\"assign\"]\n" +
                "                \n" +
                "                create o \"o1\" in [\"oa1\"]\n" +
                "                create o \"o2\" in [\"oa1\"]\n" +
                "                create o \"o3\" in [\"oa1\"]\n" +
                "                \n" +
                "                operation op1(string a, []string b) {\n" +
                "                    check \"assign\" on a\n" +
                "                    check \"assign\" on b\n" +
                "                    check \"assign\" on \"oa1\"\n" +
                "                } {\n" +
                "                    create policy class \"test\"\n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.executePML(new UserContext("u1"), "                op1(\"o1\", [\"o2\", \"o3\"])\n");
			return null;
        });
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> pdp.runTx(new UserContext("u2"), tx -> {
            tx.executePML(new UserContext("u2"), "op1(\"o1\", [\"o2\", \"o3\"])");
			return null;
        }));
    }

    @Test
    void testWithNoChecks() throws PMException {
        String pml = "create pc \"pc1\"\n" +
                "                create ua \"ua1\" in [\"pc1\"]\n" +
                "                create u \"u1\" in [\"ua1\"]\n" +
                "                create ua \"ua2\" in [\"pc1\"]\n" +
                "                create u \"u2\" in [\"ua2\"]\n" +
                "                create oa \"oa1\" in [\"pc1\"]\n" +
                "                associate \"ua1\" and \"oa1\" with [\"assign\"]\n" +
                "                \n" +
                "                create o \"o1\" in [\"oa1\"]\n" +
                "                create o \"o2\" in [\"oa1\"]\n" +
                "                create o \"o3\" in [\"oa1\"]\n" +
                "                \n" +
                "                operation op1(string a, []string b) {\n" +
                "                    create policy class a\n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.executePML(new UserContext("u1"), "op1(\"test1\", [\"o2\", \"o3\"])");

	        return null;
        });
        assertTrue(pap.query().graph().nodeExists("test1"));

        pdp.runTx(new UserContext("u2"), tx -> {
            tx.executePML(new UserContext("u2"), "op1(\"test2\", [\"o2\", \"o3\"])");

	        return null;
        });
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

}