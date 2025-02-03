package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PAPTest extends PAPTestInitializer {

    static Operation<Value> op = new Operation<Value>("testFunc") {
        @Override
        public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map operands) throws PMException {

        }

        @Override
        public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
            pap.modify().graph().createPolicyClass("pc3");
            return new VoidValue();
        }
    };

    @Test
    void testTx() throws PMException {
        pap.beginTx();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet());
        pap.commit();

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
	    assertEquals(pap.query().graph().getAssociationsWithSource(id("ua1")).iterator().next(), new Association(id("ua1"), id("oa1"), new AccessRightSet()));

        pap.beginTx();
        pap.modify().graph().deleteNode(id("ua1"));
        pap.rollback();
        assertTrue(pap.query().graph().nodeExists("ua1"));
    }

    @Test
    void testExecutePML() throws PMException {
        try {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().operations().createAdminOperation(op);

            pap.executePML(new UserContext(id("u1")), "create ua \"ua4\" in [\"Location\"]\ntestFunc()");
            assertTrue(pap.query().graph().nodeExists("ua4"));
            assertTrue(pap.query().graph().nodeExists("pc3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAdminPolicyCreatedInConstructor() throws PMException {
        testAdminPolicy(pap);
    }

    @Test
    void testResetInitializesAdminPolicy() throws PMException {
        pap.reset();

        testAdminPolicy(pap);
    }

    public static void testAdminPolicy(PAP pap) throws PMException {
        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.PM_ADMIN_PC.nodeId()));
        Collection<Long> ascendants = pap.query().graph().getAdjacentAscendants(AdminPolicyNode.PM_ADMIN_PC.nodeId());
        assertEquals(1, ascendants.size());
	    assertEquals(ascendants.iterator().next(), (AdminPolicyNode.PM_ADMIN_OBJECT.nodeId()));

        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.PM_ADMIN_OBJECT.nodeId()));
        Collection<Long> descendants = pap.query().graph().getAdjacentDescendants(AdminPolicyNode.PM_ADMIN_OBJECT.nodeId());
        assertEquals(1, descendants.size());
	    assertEquals(descendants.iterator().next(), (AdminPolicyNode.PM_ADMIN_PC.nodeId()));
    }

    @Test
    void testRecursiveOperation() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                
                associate "ua1" and PM_ADMIN_OBJECT with ["assign"]
                associate "ua1" and "ua2" with ["assign"]
                
                operation op1(nodeop string a) {
                    check "assign" on a
                } {
                    if a == PM_ADMIN_OBJECT {
                        op1("ua2")
                    }
                    
                    create pc a + "_PC"
                }
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        pap.executePML(new TestUserContext("u1"), "op1(PM_ADMIN_OBJECT)");
        assertTrue(pap.query().graph().nodeExists("ua2_PC"));
        assertTrue(pap.query().graph().nodeExists("PM_ADMIN:object_PC"));
    }

    @Test
    void testExecutePMLCreatesObligationBeforeAuthorUserThrowsException() {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                
                create obligation "o1" {
                    create rule "r1"
                    when any user 
                    performs any operation
                    do(ctx) {}
                }
                
                create u "u1" in ["ua1"]
                """;
        assertThrows(NodeDoesNotExistException.class, () -> pap.executePML(new UserContext(id("u1")), pml));
    }
}