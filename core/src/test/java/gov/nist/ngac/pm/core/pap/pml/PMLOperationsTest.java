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

package gov.nist.ngac.pm.core.pap.pml;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.epp.EPP;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public class PMLOperationsTest {

	@Test
	void testCallOperationInObligationResponse() throws PMException {
		String pml = """
                create PC "pc1"
                create UA "ua1" in ["pc1"]
                create UA "ua2" in ["pc1"]
                create U "u1" in ["ua1", "ua2"]
                associate "ua1" to "ua2" with ["*"]
                create OA "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["*"]
                associate "ua1" to PM_ADMIN_POLICY_CLASSES with ["*"]
                
                @ReqCap({
                    require ["admin:graph:assignment:ascendant:create"] on ["oa1"]
                })
                adminop op1(string name) {
                    create pc name
                }
                
                create obligation "ob1"
                    when any user
                    performs any operation
                    do(ctx) {
                    	name := ctx.opName
                    	if node_exists(node_name=name) {
                    		return
                    	}
                    	
                        op1(name=ctx.opName)
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(NodeUserContext.of("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);

		pdp.runTx(NodeUserContext.of(id("u1")), tx -> {
			tx.modify().graph().createPolicyClass("test2");

			return null;
		});

		assertTrue(pap.query().graph().nodeExists("create_policy_class"));
		assertTrue(pap.query().graph().nodeExists("op1"));
	}

	@Test
	void testCallRoutineInObligationResponse() throws PMException {
		String pml = """
                create PC "pc1"
                create UA "ua1" in ["pc1"]
                create UA "ua2" in ["pc1"]
                create U "u1" in ["ua1", "ua2"]
                create OA "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["*"]
                associate "ua1" to "ua2" with ["*"]
                associate "ua1" to PM_ADMIN_POLICY_CLASSES with ["*"]
                
                routine op1(string name) {
                    if !node_exists(node_name=name) {
                        create pc name
                    }
                }
                
                create obligation "ob1"
                    when any user
                    performs any operation
                    do(ctx) {
                        op1(name="test")
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(NodeUserContext.of("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);
		pdp.runTx(NodeUserContext.of(id("u1")), tx -> {
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
                    performs "op1"
                    do(ctx) {
                        create pc "pc3"
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(NodeUserContext.of("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);

		pdp.adjudicateOperation(NodeUserContext.of(id("u1")),
			"op2",
			Map.of());

		assertFalse(pap.query().graph().nodeExists("pc3"));
	}

	@Test
	void testCallCustomOperationInRoutineDoesTriggerObligationResponse() throws PMException {
		// call custom operation in a routine should trigger an obligation response
		String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1", "ua2"]
                associate "ua1" to "ua2" with ["*"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["*"]
                
                routine routine1() {
                    op1()
                }
                
                adminop op1() {
                    create pc "pc2"
                }
                
                create obligation "ob1"
                    when any user
                    performs "op1"
                    do(ctx) {
                        create pc "pc3"
                    }
                """;
		MemoryPAP pap = new TestPAP();
		pap.executePML(NodeUserContext.of("u1"), pml);

		PDP pdp = new PDP(pap);
		EPP epp = new EPP(pdp, pap);
		epp.subscribeTo(pdp);

		pdp.adjudicateOperation(NodeUserContext.of(id("u1")), "routine1", Map.of());

		assertTrue(pap.query().graph().nodeExists("pc3"));
	}

	@Test
	void testFunctionOnlyAllowsBasicStatements() throws PMException {
		String pml = """
		function fail() {
			create pc "pc1"
		}
		""";

		PAP pap = new TestPAP();
		assertThrows(PMException.class, () -> pap.executePML(NodeUserContext.of("u1"), pml));
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
		assertThrows(PMException.class, () -> pap.executePML(NodeUserContext.of("u1"), pml));
	}

	@Test
	void testFunctionInFunctionOk() throws PMException {
		String pml = """
		function ok1() string {
			function ok2(string a) string {
				return a
			}
			
			return ok2(a="a") + ok2(a="b")
		}
		
		create pc ok1()
		""";

		PAP pap = new TestPAP();
		pap.executePML(NodeUserContext.of("u1"), pml);

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
			
			op1(x="end")
		}
		
		op1(x="start")
		""";

		PAP pap = new TestPAP();
		pap.executePML(NodeUserContext.of("u1"), pml);

		assertTrue(pap.query().graph().nodeExists("start"));
		assertTrue(pap.query().graph().nodeExists("end"));
	}

}
