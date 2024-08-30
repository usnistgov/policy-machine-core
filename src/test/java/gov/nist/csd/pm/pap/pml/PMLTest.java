package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pdp.AdminAdjudicationResponse;
import gov.nist.csd.pm.pdp.OperationRequest;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pdp.Decision.DENY;
import static gov.nist.csd.pm.pdp.Decision.GRANT;
import static org.junit.jupiter.api.Assertions.*;

public class PMLTest {

    @Test
    void testCallingNonPMLOperationAndRoutineFromPMLWithOperandsAndReturnValue() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["assign"]
                
                create prohibition "pro1"
                deny user "u2"
                access rights ["assign"]
                on union of [ADMIN_POLICY_OBJECT]
                """);

        Operation<?> op1 = new Operation<>("op1", List.of("a", "b", "c")) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
                privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), "assign");
            }

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                String a = (String) operands.get("a");
                List<String> b = (List<String>) operands.get("b");
                Map<String, String> c = (Map<String, String>) operands.get("c");

                pap.modify().graph().createPolicyClass("1" + a);

                for (String b1 : b) {
                    pap.modify().graph().createPolicyClass("1" + b1);
                }

                for (Map.Entry<String, String> c1 : c.entrySet()) {
                    pap.modify().graph().createPolicyClass("1" + c1.getKey());
                    pap.modify().graph().createPolicyClass("1" + c1.getValue());
                }

                return null;
            }
        };
        pap.modify().operations().createAdminOperation(op1);

        pap.modify().routines().createAdminRoutine(new Routine<>("routine1", List.of("a", "b", "c")) {
            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.executeAdminExecutable(op1, operands);

                return null;
            }
        });

        PDP pdp = new PDP(pap);
        pdp.executePML(new UserContext("u1"), """
                op1("a", ["b", "c"], {"d": "e", "f": "g"})
                """);
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));

        assertThrows(UnauthorizedException.class, () -> pdp.executePML(new UserContext("u2"), """
                op1("a", ["b", "c"], {"d": "e", "f": "g"})
                """));

        pdp.executePML(new UserContext("u1"), """
                routine1("1", ["2", "3"], {"4": "5", "6": "7"})
                """);
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        assertThrows(UnauthorizedException.class, () -> pdp.executePML(new UserContext("u2"), """
                routine1("1", ["2", "3"], {"4": "5", "6": "7"})
                """));
    }

    @Test
    void testCallPMLOperationAndRoutineFromNonPML() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["assign"]
                
                create prohibition "pro1"
                deny user "u2"
                access rights ["assign"]
                on union of [ADMIN_POLICY_OBJECT]
                
                operation op1(string a, []string b, map[string]string c) {
                    check "assign" on ADMIN_POLICY_OBJECT
                } {
                    create pc "1" + a
                    
                    foreach x in b {
                        create pc "1" + x
                    }
                    
                    foreach x, y in c {
                        create pc "1" + x
                        create pc "1" + y
                    }
                }
                
                routine routine1(string a, []string b, map[string]string c) {
                    op1(a, b, c)
                }
                """);

        PDP pdp = new PDP(pap);
        AdminAdjudicationResponse response = pdp.adjudicateAdminOperations(new UserContext("u1"), List.of(new OperationRequest("op1",
                Map.of("a", "a", "b", List.of("b", "c"), "c", Map.of("d", "e", "f", "g")))));
        assertEquals(GRANT, response.getDecision());
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));


        response = pdp.adjudicateAdminOperations(new UserContext("u2"), List.of(new OperationRequest("op1",
                Map.of("a", "a", "b", List.of("b", "c"), "c", Map.of("d", "e", "f", "g")))));
        assertEquals(DENY, response.getDecision());

        response = pdp.adjudicateAdminOperations(new UserContext("u1"), List.of(new OperationRequest("op1",
                Map.of("a", "1", "b", List.of("2", "3"), "c", Map.of("4", "5", "6", "7")))));
        assertEquals(GRANT, response.getDecision());
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        response = pdp.adjudicateAdminOperations(new UserContext("u2"), List.of(new OperationRequest("op1",
                Map.of("a", "1", "b", List.of("2", "3"), "c", Map.of("4", "5", "6", "7")))));
        assertEquals(DENY, response.getDecision());
    }

}
