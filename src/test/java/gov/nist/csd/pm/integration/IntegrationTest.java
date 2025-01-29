package gov.nist.csd.pm.integration;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.util.TestMemoryPAP;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
                associate "ua1" and PM_ADMIN_OBJECT with ["*"]
                
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
        TestMemoryPAP pap = new TestMemoryPAP();
        pap.executePML(new UserContext(pap.id("u1")), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.runTx(new UserContext(pap.id("u1")), tx -> {
            tx.modify().graph().createPolicyClass("test2");

            return null;
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
                associate "ua1" and PM_ADMIN_OBJECT with ["*"]
                
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
        TestMemoryPAP pap = new TestMemoryPAP();
        pap.executePML(new UserContext(pap.id("u1")), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.runTx(new UserContext(pap.id("u1")), tx -> {
            tx.modify().graph().createPolicyClass("test2");
            return null;
        });

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

    @Test
    void testCallRoutineInOperationDoesNotTriggerObligationResponse() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                routine routine1() {
                    op1()
                }
                
                operation op1() {
                    create pc "pc2"
                }
                
                operation op2() {
                    routine1()
                }
                
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) {
                        create pc "pc3"
                    }
                }
                """;
        TestMemoryPAP pap = new TestMemoryPAP();
        pap.executePML(new UserContext(pap.id("u1")), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        pdp.adjudicateAdminOperation(new UserContext(pap.id("u1")), "op2", Map.of());

        assertFalse(pap.query().graph().nodeExists("pc3"));
    }

    @Test
    void testCallCustomOperationInRoutineDoesTriggerObligationResponse() throws PMException {
        // call custom operation in a routine should trigger an obligation response
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                routine routine1() {
                    op1()
                }
                
                operation op1() {
                    create pc "pc2"
                }
                
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) {
                        create pc "pc3"
                    }
                }
                """;
        TestMemoryPAP pap = new TestMemoryPAP();
        pap.executePML(new UserContext(pap.id("u1")), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        pdp.adjudicateAdminRoutine(new UserContext(pap.id("u1")), "routine1", Map.of());

        assertFalse(pap.query().graph().nodeExists("pc3"));
    }
}
