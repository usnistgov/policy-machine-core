package gov.nist.csd.pm.integration;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    @Test
    void testCallOperationInObligationResponse() throws PMException {
        String pml = """
                create PC "pc1"
                create UA "ua1" in ["pc1"]
                create U "u1" in ["ua1"]
                create OA "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["*"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*"]
                
                operation op1(string name) {
                    check "assign" on "oa1"
                } {
                    create pc name
                }
                
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) {
                        op1("test")
                    }
                }
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.modify().graph().createPolicyClass("test2");
        });

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

    @Test
    void testCallRoutineInObligationResponse() throws PMException {
        String pml = """
                create PC "pc1"
                create UA "ua1" in ["pc1"]
                create U "u1" in ["ua1"]
                create OA "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["*"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*"]
                
                routine op1(string name) {
                    if !nodeExists(name) {
                        create pc name
                    }
                }
                
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) {
                        op1("test")
                    }
                }
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.modify().graph().createPolicyClass("test2");
        });

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }
}
