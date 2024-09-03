package gov.nist.csd.pm.integration;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.OperationRequest;
import gov.nist.csd.pm.pdp.PDP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    @Test
    void testCallOperationInObligationResponse() throws PMException {
        String pml = "create PC \"pc1\"\n" +
                "                create UA \"ua1\" in [\"pc1\"]\n" +
                "                create U \"u1\" in [\"ua1\"]\n" +
                "                create OA \"oa1\" in [\"pc1\"]\n" +
                "                associate \"ua1\" and \"oa1\" with [\"*\"]\n" +
                "                associate \"ua1\" and PM_ADMIN_OBJECT with [\"*\"]\n" +
                "                \n" +
                "                operation op1(string name) {\n" +
                "                    check \"assign\" on \"oa1\"\n" +
                "                } {\n" +
                "                    create pc name\n" +
                "                }\n" +
                "                \n" +
                "                create obligation \"ob1\" {\n" +
                "                    create rule \"r1\"\n" +
                "                    when any user\n" +
                "                    performs any operation\n" +
                "                    do(ctx) {\n" +
                "                        op1(\"test\")\n" +
                "                    }\n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.modify().graph().createPolicyClass("test2");

            return null;
        });

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

    @Test
    void testCallRoutineInObligationResponse() throws PMException {
        String pml = "create PC \"pc1\"\n" +
                "                create UA \"ua1\" in [\"pc1\"]\n" +
                "                create U \"u1\" in [\"ua1\"]\n" +
                "                create OA \"oa1\" in [\"pc1\"]\n" +
                "                associate \"ua1\" and \"oa1\" with [\"*\"]\n" +
                "                associate \"ua1\" and PM_ADMIN_OBJECT with [\"*\"]\n" +
                "                \n" +
                "                routine op1(string name) {\n" +
                "                    if !nodeExists(name) {\n" +
                "                        create pc name\n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                create obligation \"ob1\" {\n" +
                "                    create rule \"r1\"\n" +
                "                    when any user\n" +
                "                    performs any operation\n" +
                "                    do(ctx) {\n" +
                "                        op1(\"test\")\n" +
                "                    }\n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.modify().graph().createPolicyClass("test2");
            return null;
        });

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

    @Test
    void testCallRoutineInOperationDoesNotTriggerObligationResponse() throws PMException {
        String pml = "create pc \"pc1\"\n" +
                "                create ua \"ua1\" in [\"pc1\"]\n" +
                "                create u \"u1\" in [\"ua1\"]\n" +
                "                \n" +
                "                routine routine1() {\n" +
                "                    op1()\n" +
                "                }\n" +
                "                \n" +
                "                operation op1() {\n" +
                "                    create pc \"pc2\"\n" +
                "                }\n" +
                "                \n" +
                "                operation op2() {\n" +
                "                    routine1()\n" +
                "                }\n" +
                "                \n" +
                "                create obligation \"ob1\" {\n" +
                "                    create rule \"r1\"\n" +
                "                    when any user\n" +
                "                    performs any operation\n" +
                "                    do(ctx) {\n" +
                "                        create pc \"pc3\"\n" +
                "                    }\n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        pdp.adjudicateAdminOperation(new UserContext("u1"), "op2", Map.of());

        assertFalse(pap.query().graph().nodeExists("pc3"));
    }

    @Test
    void testCallCustomOperationInRoutineDoesTriggerObligationResponse() throws PMException {
        // call custom operation in a routine should trigger an obligation response
        String pml = "create pc \"pc1\"\n" +
                "                create ua \"ua1\" in [\"pc1\"]\n" +
                "                create u \"u1\" in [\"ua1\"]\n" +
                "                \n" +
                "                routine routine1() {\n" +
                "                    op1()\n" +
                "                }\n" +
                "                \n" +
                "                operation op1() {\n" +
                "                    create pc \"pc2\"\n" +
                "                }\n" +
                "                \n" +
                "                create obligation \"ob1\" {\n" +
                "                    create rule \"r1\"\n" +
                "                    when any user\n" +
                "                    performs any operation\n" +
                "                    do(ctx) {\n" +
                "                        create pc \"pc3\"\n" +
                "                    }\n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        pdp.adjudicateAdminRoutine(new UserContext("u1"), "routine1", Map.of());

        assertFalse(pap.query().graph().nodeExists("pc3"));
    }
}
