package gov.nist.csd.pm.core.pap.pml;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class PMLBasicFunctionsTest {

	@Test
	void testCallOperationInObligationResponse() throws PMException {
		String pml = """
                create PC "pc1"
                create UA "ua1" in ["pc1"]
                create U "u1" in ["ua1"]
                create OA "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["*"]
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["*"]
                
                adminop op1(string name) {
                    check ["assign"] on ["oa1"]
                    create pc name
                }
                
                create obligation "ob1"
                    when any user
                    performs any operation
                    do(ctx) {
                        op1("test")
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(new TestUserContext("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);

		pdp.runTx(new UserContext(id("u1")), tx -> {
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
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["*"]
                
                routine op1(string name) {
                    if !nodeExists(name) {
                        create pc name
                    }
                }
                
                create obligation "ob1"
                    when any user
                    performs any operation
                    do(ctx) {
                        op1("test")
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(new TestUserContext("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);
		pdp.runTx(new UserContext(id("u1")), tx -> {
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
                
                adminop op2() {
                    routine1()
                }
                
                routine routine1() {
                    op1()
                }
                
                adminop op1() {
                    create pc "pc2"
                }
                
                create obligation "ob1"
                    when any user
                    performs op1
                    do(ctx) {
                        create pc "pc3"
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(new TestUserContext("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);

		pdp.adjudicateAdminOperation(new UserContext(id("u1")),
			"op2",
			Map.of());

		assertFalse(pap.query().graph().nodeExists("pc3"));
	}

	@Test
	void testCallCustomOperationInRoutineDoesTriggerObligationResponse() throws PMException {
		// call custom operation in a function should trigger an obligation response
		String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                routine routine1() {
                    op1()
                }
                
                adminop op1() {
                    create pc "pc2"
                }
                
                create obligation "ob1"
                    when any user
                    performs any operation
                    do(ctx) {
                        create pc "pc3"
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(new TestUserContext("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);

		pdp.adjudicateAdminRoutine(new UserContext(id("u1")), "routine1", Map.of());

		assertFalse(pap.query().graph().nodeExists("pc3"));
	}

	@Test
	void testFunctionOnlyAllowsBasicStatements() throws PMException {
		String pml = """
		function fail() {
			create pc "pc1"
		}
		""";

		PAP pap = new TestPAP();
		assertThrows(PMException.class, () -> pap.executePML(new TestUserContext("u1"), pml));
	}

	@Test
	void testFunctionOnlyAllowsFunctionInvokesOnly() throws PMException {
		String pml = """
		adminop op1() {}
		
		function fail() {
			op1()
		}
		""";

		PAP pap = new TestPAP();
		assertThrows(PMException.class, () -> pap.executePML(new TestUserContext("u1"), pml));
	}

	@Test
	void testFunctionInFunctionOk() throws PMException {
		String pml = """
		function ok1() string {
			function ok2(string a) string {
				return a
			}
			
			return ok2("a") + ok2("b")
		}
		
		create pc ok1()
		""";

		PAP pap = new TestPAP();
		pap.executePML(new TestUserContext("u1"), pml);

		assertTrue(pap.query().graph().nodeExists("ab"));
	}

	@Test
	void testRecursiveCall() throws PMException {
		String pml = """
		adminop op1(string x) {
			create pc x
			
			if x == "end" {
				return
			}
			
			op1("end")
		}
		
		op1("start")
		""";

		PAP pap = new TestPAP();
		pap.executePML(new TestUserContext("u1"), pml);

		assertTrue(pap.query().graph().nodeExists("start"));
		assertTrue(pap.query().graph().nodeExists("end"));
	}

}
