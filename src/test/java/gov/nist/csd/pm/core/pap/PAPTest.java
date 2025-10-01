package gov.nist.csd.pm.core.pap;

import gov.nist.csd.pm.core.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;
import gov.nist.csd.pm.core.util.SamplePolicy;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.*;

public abstract class PAPTest extends PAPTestInitializer {

    public static final FormalParameter<String> ARG_A = new FormalParameter<>("a", STRING_TYPE);
    public static final FormalParameter<String> ARG_B = new FormalParameter<>("b", STRING_TYPE);

    static Operation<Object, Args> op = new Operation<>("testFunc", List.of()) {
        @Override
        public void canExecute(PAP pap, UserContext userCtx, Args args) {

        }

        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            pap.modify().graph().createPolicyClass("pc3");
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };

    @Test
    void testBootstrapHasAdminNodes() throws PMException {
        assertDoesNotThrow(() -> pap.bootstrap(new PolicyBootstrapper() {
            @Override
            public void bootstrap(PAP pap) throws PMException {
                pap.modify().graph().createUserAttribute("ua1", List.of(AdminPolicyNode.PM_ADMIN_PC.nodeId()));
            }
        }));

        pap.modify().graph().createPolicyClass("test");
        assertThrows(BootstrapExistingPolicyException.class, () -> pap.bootstrap(new PolicyBootstrapper() {
            @Override
            public void bootstrap(PAP pap) throws PMException {
                pap.modify().graph().createUserAttribute("ua1", List.of(AdminPolicyNode.PM_ADMIN_PC.nodeId()));
            }
        }));
    }

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
	    assertEquals(ascendants.iterator().next(), (AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId()));

        assertTrue(pap.query().graph().nodeExists(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId()));
        Collection<Long> descendants = pap.query().graph().getAdjacentDescendants(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId());
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
                
                associate "ua1" and PM_ADMIN_BASE_OA with ["assign"]
                associate "ua1" and "ua2" with ["assign"]
                
                operation op1(@node string a) {
                    check "assign" on [a]
                } {
                    if a == PM_ADMIN_BASE_OA {
                        op1("ua2")
                    }
                    
                    create pc a + "_PC"
                }
                """;
        pap.executePML(new TestUserContext("u1"), pml);

        pap.executePML(new TestUserContext("u1"), "op1(PM_ADMIN_BASE_OA)");
        assertTrue(pap.query().graph().nodeExists("ua2_PC"));
        assertTrue(pap.query().graph().nodeExists("PM_ADMIN:base_PC"));
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

    @Test
    void testPluginRegistry() {
        pap.plugins().registerOperation(new Operation<>("op1", List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return null;
            }
        });

        pap.plugins().registerRoutine(new Routine<>("routine1", List.of()) {
            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return null;
            }
        });

        assertTrue(pap.plugins().getOperationNames().contains("op1"));
        assertTrue(pap.plugins().getRoutineNames().contains("routine1"));
    }
}