package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

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

            pap.modify().operations().createAdminOperation(op);

            pap.executePML(new UserContext("u1"), "create ua \"ua4\" in [\"Location\"]\ntestFunc()");
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
        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.PM_ADMIN_PC.nodeName()));
        Collection<String> ascendants = pap.query().graph().getAdjacentAscendants(AdminPolicyNode.PM_ADMIN_PC.nodeName());
        assertEquals(1, ascendants.size());
        assertTrue(ascendants.contains(AdminPolicyNode.PM_ADMIN_OBJECT.nodeName()));

        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.PM_ADMIN_OBJECT.nodeName()));
        Collection<String> descendants = pap.query().graph().getAdjacentDescendants(AdminPolicyNode.PM_ADMIN_OBJECT.nodeName());
        assertEquals(1, descendants.size());
        assertTrue(descendants.contains(AdminPolicyNode.PM_ADMIN_PC.nodeName()));
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
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        pap.executePML(new UserContext("u1"), "op1(PM_ADMIN_OBJECT)");
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