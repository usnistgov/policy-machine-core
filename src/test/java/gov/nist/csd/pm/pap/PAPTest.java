package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PAPTest extends PAPTestInitializer {

    @Test
    void testTx() throws PMException {
        pap.beginTx();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().associate("ua1", "oa1", new AccessRightSet());
        pap.commit();

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().getAssociationsWithSource("ua1").iterator().next()
                      .equals(new Association("ua1", "oa1", new AccessRightSet())));

        pap.beginTx();
        pap.modify().graph().deleteNode("ua1");
        pap.rollback();
        assertTrue(pap.query().graph().nodeExists("ua1"));
    }

    @Test
    void testExecutePML() throws PMException {
        try {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().operations().createAdminOperation(new Operation<Value>("testFunc") {
                @Override
                public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map operands) throws PMException {

                }

                @Override
                public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
                    pap.modify().graph().createPolicyClass("pc3");
                    return new VoidValue();
                }
            });

            pap.executePML(new UserContext("u1"), "create ua \"ua4\" in [\"Location\"]\ntestFunc()");
            assertTrue(pap.query().graph().nodeExists("ua4"));
            assertTrue(pap.query().graph().nodeExists("pc3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAdminPolicyCreatedInConstructor() throws PMException {
        testAdminPolicy(pap, 1);
    }

    @Test
    void testResetInitializesAdminPolicy() throws PMException {
        pap.reset();

        testAdminPolicy(pap, 1);
    }

    public static void testAdminPolicy(PAP pap, int numExpectedPolicyClasses) throws PMException {
        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.ADMIN_POLICY.nodeName()));
        Collection<String> ascendants = pap.query().graph().getAdjacentAscendants(AdminPolicyNode.ADMIN_POLICY.nodeName());
        assertEquals(1, ascendants.size());
        assertTrue(ascendants.contains(AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName()));

        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName()));
        Collection<String> descendants = pap.query().graph().getAdjacentDescendants(AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName());
        assertEquals(1, descendants.size());
        assertTrue(descendants.contains(AdminPolicyNode.ADMIN_POLICY.nodeName()));
    }

    @Test
    void testRecursiveOperation() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                
                associate "ua1" and ADMIN_POLICY_OBJECT with ["assign"]
                associate "ua1" and "ua2" with ["assign"]
                
                operation op1(nodeop string a) {
                    check "assign" on a
                } {
                    if a == ADMIN_POLICY_OBJECT {
                        op1("ua2")
                    }
                    
                    create pc a + "_PC"
                }
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        pap.executePML(new UserContext("u1"), "op1(ADMIN_POLICY_OBJECT)");
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
        assertThrows(NodeDoesNotExistException.class, () -> pap.executePML(new UserContext("u1"), pml));
    }
}