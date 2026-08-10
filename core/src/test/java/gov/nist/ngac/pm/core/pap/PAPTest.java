package gov.nist.ngac.pm.core.pap;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.BootstrapExistingPolicyException;
import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pdp.bootstrap.PMLBootstrapper;
import gov.nist.ngac.pm.core.pdp.bootstrap.PolicyBootstrapper;
import gov.nist.ngac.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class PAPTest extends PAPTestInitializer {

    public static final FormalParameter<String> ARG_A = new FormalParameter<>("a", STRING_TYPE);
    public static final FormalParameter<String> ARG_B = new FormalParameter<>("b", STRING_TYPE);

    static AdminOperation<Void> op = new AdminOperation<>("testFunc", VOID_TYPE, List.of(), List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            pap.modify().graph().createPolicyClass("pc3");
            return null;
        }

    };

    // must be static: an anonymous class defined inside a non-static test method captures the
    // enclosing test instance, which breaks Neo4j's Java-serialization write path
    static AdminOperation<Void> javaOp1 = new AdminOperation<>("javaOp1", VOID_TYPE, List.of(), List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> javaRoutine1 = new Routine<>("javaRoutine1", VOID_TYPE, List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
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

            pap.javaOperations().register(op);
            pap.modify().operations().createOperation(op);

            pap.executePML(NodeUserContext.of(id("u1")), "create ua \"ua4\" in [\"Location\"]\ntestFunc()");
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
                
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:ascendant:create"]
                associate "ua1" to "ua2" with ["admin:graph:assignment:ascendant:create"]
                
                @ReqCap({
                    require ["admin:graph:assignment:ascendant:create"] on [a]
                })
                adminop op1(@Node string a) {
                    if a == PM_ADMIN_BASE_OA {
                        op1(a="ua2")
                    }
                    
                    create pc a + "_PC"
                }
                """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        pap.executePML(NodeUserContext.of("u1"), "op1(a=PM_ADMIN_BASE_OA)");
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
        assertThrows(NodeDoesNotExistException.class, () -> pap.executePML(NodeUserContext.of(id("u1")), pml));
    }

    @Test
    void testJavaOperationRegistrationLifecycle() throws PMException {
        pap.javaOperations().register(javaOp1);
        pap.javaOperations().register(javaRoutine1);

        // registering alone makes the implementation available in-process, but has no policy
        // effect until createOperation persists a reference to it
        assertFalse(pap.query().operations().getOperations().containsAll(List.of(javaOp1, javaRoutine1)));

        pap.modify().operations().createOperation(javaOp1);
        pap.modify().operations().createOperation(javaRoutine1);

        assertTrue(pap.query().operations().getOperations().containsAll(List.of(javaOp1, javaRoutine1)));
    }

    @Test
    void testBootstrapDoesNotThrowExceptionWhenJavaOperationRegistryHasRegistrations() throws PMException {
        pap.javaOperations().register(new AdminOperation<>("op1", VOID_TYPE, List.of(), List.of()) {
            @Override
            public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
                return null;
            }
        });

        assertDoesNotThrow(() -> pap.bootstrap(new PMLBootstrapper("u1", """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            assign "u1" to ["ua1"]
            """)));
    }
}