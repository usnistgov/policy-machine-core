package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.PolicyEquals;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.CreateAttrStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.toProperties;
import static org.junit.jupiter.api.Assertions.*;

public abstract class PAPTest {

    PAP pap;

    public abstract PAP getPAP() throws PMException;

    @BeforeEach
    void setup() throws PMException {
        pap = getPAP();
    }

    @Test
    void testTx() throws PMException {
        pap.beginTx();
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().associate("ua1", "oa1", new AccessRightSet());
        pap.commit();

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));
        assertTrue(pap.graph().nodeExists("ua1"));
        assertTrue(pap.graph().getAssociationsWithSource("ua1").contains(new Association("ua1", "oa1")));

        pap.beginTx();
        pap.graph().deleteNode("ua1");
        pap.rollback();
        assertTrue(pap.graph().nodeExists("ua1"));
    }

    private static final String input = """
            create policy class 'super_policy'
            create user attribute 'super_ua' in ['super_policy']
            associate 'super_ua' and ADMIN_POLICY_TARGET with ['*']
            associate 'super_ua' and POLICY_CLASSES_OA with ['*']
            associate 'super_ua' and PML_FUNCTIONS_TARGET with ['*']
            associate 'super_ua' and PML_CONSTANTS_TARGET with ['*']
            create user attribute 'super_ua1' in ['super_policy']
            associate 'super_ua' and 'super_ua1' with ['*']
            create user 'super' in ['super_ua']
            assign 'super' to ['super_ua1']
            
            set resource access rights ['read', 'write', 'execute']
            create policy class 'pc1'
            set properties of 'pc1' to {'k':'v'}
            create oa 'oa1' in ['pc1']
            set properties of 'oa1' to {'k1':'v1', 'k2':'v2'}
            create ua 'ua1' in ['pc1']
            associate 'ua1' and 'oa1' with ['read', 'write']
            create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
            create obligation 'obl1' {
                create rule 'rule1'
                when any user
                performs ['event1', 'event2']
                do(evtCtx) {
                    let event = evtCtx['event']
                    if equals(event, 'event1') {
                        create policy class 'e1'
                    } else if equals(event, 'event2') {
                        create policy class 'e2'
                    }
                }
            }
            const testConst = "hello world"
            function testFunc() void {
                create pc "pc1"
            }
            """;
    private static final String expected = """
            # constants
            const testConst = 'hello world'
                        
            # functions
            function testFunc() void {create policy class 'pc1'}
                        
            # graph
            set resource access rights ['read', 'write', 'execute']
                        
            # policy class: pm_admin:policy
            create object attribute 'pc1:target' in [POLICY_CLASSES_OA]
            create object attribute 'super_policy:target' in [POLICY_CLASSES_OA]
                        
            # policy class: super_policy
            create policy class 'super_policy'
            create user attribute 'super_ua' in ['super_policy']
            create user attribute 'super_ua1' in ['super_policy']
            associate 'super_ua' and 'super_ua1' with ['*']
            associate 'super_ua' and PML_FUNCTIONS_TARGET with ['*']
            associate 'super_ua' and POLICY_CLASSES_OA with ['*']
            associate 'super_ua' and ADMIN_POLICY_TARGET with ['*']
            associate 'super_ua' and PML_CONSTANTS_TARGET with ['*']
                        
            # policy class: pc1
            create policy class 'pc1'
            set properties of 'pc1' to {'k': 'v'}
            create user attribute 'ua1' in ['pc1']
            create object attribute 'oa1' in ['pc1']
            set properties of 'oa1' to {'k1': 'v1', 'k2': 'v2'}
            associate 'ua1' and 'oa1' with ['read', 'write']
                        
            # users
            create user 'super' in ['super_ua', 'super_ua1']
                        
            # objects
                        
            # prohibitions
            create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
                        
            # obligations
            create obligation 'obl1' {create rule 'rule1' when any user performs ['event1', 'event2'] on any policy element do (evtCtx) {let event = evtCtx['event']if equals(event, 'event1') {create policy class 'e1'} else if equals(event, 'event2') {create policy class 'e2'} }}
            """.trim();
    @Test
    void testSerialize() throws PMException {
        pap.deserialize().fromPML(new UserContext(SUPER_USER), input);
        String actual = pap.serialize().toPML();
        assertEquals(new ArrayList<>(), pmlEqual(expected, actual));

        pap.deserialize().fromPML(new UserContext(SUPER_USER), actual);
        actual = pap.serialize().toPML();
        assertEquals(new ArrayList<>(), pmlEqual(expected, actual));

        String json = pap.serialize().toJSON();
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        memoryPolicyStore.deserialize().fromJSON(json);
        pap.deserialize().fromJSON(json);
        PolicyEquals.check(pap, memoryPolicyStore);
    }

    private List<String> pmlEqual(String expected, String actual) {
        List<String> expectedLines = sortLines(expected);
        List<String> actualLines = sortLines(actual);
        expectedLines.removeIf(line -> actualLines.contains(line));
        return expectedLines;
    }

    private List<String> sortLines(String pml) {
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(pml);
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        Collections.sort(lines);
        return lines;
    }

    @Test
    void testExecutePML() throws PMException {
        try {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement(
                    "testfunc",
                    Type.voidType(),
                    List.of(),
                    (ctx, policy) -> {
                        policy.graph().createPolicyClass("pc3");
                        return new Value();
                    }
            );

            pap.executePML(new UserContext(SUPER_USER), "create ua 'ua3' in ['pc2']\ntestfunc()", functionDefinitionStatement);
            assertTrue(pap.graph().nodeExists("ua3"));
            assertTrue(pap.graph().nodeExists("pc3"));
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

    @Test
    void testBootstrapWithAdminPolicyOnly() throws PMException {
        pap.bootstrap(p -> {
            p.graph().createPolicyClass("pc1");
        });

        testAdminPolicy(pap, 2);
        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists(AdminPolicy.policyClassTargetName("pc1")));
    }

    @Test
    void testBootstrapWithExistingPolicyThrowsException() throws PMException {
        pap.graph().createPolicyClass("pc1");
        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pap.bootstrap(new SuperUserBootstrapper());
        });

        pap.reset();

        pap.graph().setResourceAccessRights(new AccessRightSet("read"));
        pap.graph().createPolicyClass("pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createUser("u1", "ua1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createObject("o1", "oa1");

        pap.prohibitions().create("pro1", new ProhibitionSubject("u1", ProhibitionSubject.Type.USER),
                                  new AccessRightSet("read"), true, new ContainerCondition("oa1", false));

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pap.bootstrap(new SuperUserBootstrapper());
        });

        pap.obligations().create(new UserContext("u1"), "obl1");

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pap.bootstrap(new SuperUserBootstrapper());
        });
    }

    void testAdminPolicy(PAP pap, int numExpectedPolicyClasses) throws PMException {
        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.ADMIN_POLICY.nodeName()));
        List<String> children = pap.graph().getChildren(AdminPolicy.Node.ADMIN_POLICY.nodeName());
        assertEquals(5, children.size());
        assertTrue(children.containsAll(List.of(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName(), AdminPolicy.Node.PML_FUNCTIONS_TARGET.nodeName(),
                                                AdminPolicy.Node.PML_CONSTANTS_TARGET.nodeName(), AdminPolicy.Node.PROHIBITIONS_TARGET.nodeName(), AdminPolicy.Node.OBLIGATIONS_TARGET.nodeName())));

        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.ADMIN_POLICY_TARGET.nodeName()));
        List<String> parents = pap.graph().getParents(AdminPolicy.Node.ADMIN_POLICY_TARGET.nodeName());
        assertEquals(1, parents.size());
        assertTrue(parents.contains(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName()));

        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName()));
        children = pap.graph().getChildren(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName());
        assertEquals(numExpectedPolicyClasses, children.size());
        assertTrue(children.contains(AdminPolicy.Node.ADMIN_POLICY_TARGET.nodeName()));

        parents = pap.graph().getParents(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName());
        assertEquals(1, parents.size());
        assertTrue(parents.contains(AdminPolicy.Node.ADMIN_POLICY.nodeName()));

        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.PML_FUNCTIONS_TARGET.nodeName()));
        parents = pap.graph().getParents(AdminPolicy.Node.PML_FUNCTIONS_TARGET.nodeName());
        assertEquals(1, parents.size());
        assertTrue(parents.contains(AdminPolicy.Node.ADMIN_POLICY.nodeName()));

        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.PML_CONSTANTS_TARGET.nodeName()));
        parents = pap.graph().getParents(AdminPolicy.Node.PML_CONSTANTS_TARGET.nodeName());
        assertEquals(1, parents.size());
        assertTrue(parents.contains(AdminPolicy.Node.ADMIN_POLICY.nodeName()));

        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.PROHIBITIONS_TARGET.nodeName()));
        parents = pap.graph().getParents(AdminPolicy.Node.PROHIBITIONS_TARGET.nodeName());
        assertEquals(1, parents.size());
        assertTrue(parents.contains(AdminPolicy.Node.ADMIN_POLICY.nodeName()));

        assertTrue(pap.graph().nodeExists(AdminPolicy.Node.OBLIGATIONS_TARGET.nodeName()));
        parents = pap.graph().getParents(AdminPolicy.Node.OBLIGATIONS_TARGET.nodeName());
        assertEquals(1, parents.size());
        assertTrue(parents.contains(AdminPolicy.Node.ADMIN_POLICY.nodeName()));
    }


    @Nested
    class GraphStoreTests {

        @Nested
        class SetResourceAccessRights {

            @Test
            void testAdminAccessRightExistsException() {
                assertThrows(AdminAccessRightExistsException.class, () ->
                        pap.graph().setResourceAccessRights(new AccessRightSet(CREATE_POLICY_CLASS)));
            }

            @Test
            void testSuccess() throws PMException {
                AccessRightSet arset = new AccessRightSet("read", "write");
                pap.graph().setResourceAccessRights(arset);
                assertEquals(arset, pap.graph().getResourceAccessRights());
            }

        }

        @Nested
        class GetResourceAccessRights {
            @Test
            void testGetResourceAccessRights() throws PMException {
                AccessRightSet arset = new AccessRightSet("read", "write");
                pap.graph().setResourceAccessRights(arset);
                assertEquals(arset, pap.graph().getResourceAccessRights());
            }
        }

        @Nested
        class CreatePolicyClassTest {
            @Test
            void testNodeNameExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                assertDoesNotThrow(() -> pap.graph().createPolicyClass("pc2"));
                assertThrows(NodeNameExistsException.class, () -> pap.graph().createPolicyClass("pc1"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                String rep = AdminPolicy.policyClassTargetName("pc1");
                assertTrue(pap.graph().nodeExists("pc1"));
                assertTrue(pap.graph().nodeExists(rep));
                assertTrue(pap.graph().getParents(rep).contains(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName()));
                assertTrue(pap.graph().getChildren(AdminPolicy.Node.POLICY_CLASSES_OA.nodeName()).contains(rep));
            }
        }

        @Nested
        class CreateObjectAttribute {

            @Test
            void testNodeNameExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(NodeNameExistsException.class,
                             () -> pap.graph().createObjectAttribute("oa1", "pc1"));
            }

            @Test
            void testNodeDoesNotExistException() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createObjectAttribute("oa1", "pc1"));

                pap.graph().createPolicyClass("pc1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createObjectAttribute("oa1", "pc1", "pc2"));
            }

            @Test
            void testInvalidAssignmentException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                             () -> pap.graph().createObjectAttribute("oa1", "ua1"));
            }

            @Test
            void testAssignmentCausesLoopException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "oa1");

                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createObjectAttribute("oa3", "oa3"));
                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createObjectAttribute("oa3", "oa2", "oa3"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", toProperties("k", "v"), "oa1");

                assertTrue(pap.graph().nodeExists("oa1"));
                assertTrue(pap.graph().nodeExists("oa2"));
                assertEquals("v", pap.graph().getNode("oa2").getProperties().get("k"));

                assertTrue(pap.graph().getChildren("pc1").contains("oa1"));
                assertTrue(pap.graph().getChildren("oa1").contains("oa2"));

                assertTrue(pap.graph().getParents("oa1").contains("pc1"));
                assertTrue(pap.graph().getParents("oa2").contains("oa1"));
            }
        }

        @Nested
        class CreateUserAttributeTest {

            @Test
            void testNodeNameExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                assertThrows(NodeNameExistsException.class,
                             () -> pap.graph().createObjectAttribute("ua1", "pc1"));
            }

            @Test
            void testNodeDoesNotExistException() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createUserAttribute("ua1", "pc1"));

                pap.graph().createPolicyClass("pc1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createUserAttribute("ua1", "pc1", "pc2"));
            }

            @Test
            void testInvalidAssignmentException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                             () -> pap.graph().createUserAttribute("ua1", "oa1"));
            }

            @Test
            void testAssignmentCausesLoopException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "ua1");

                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createUserAttribute("ua3", "ua3"));
                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createUserAttribute("ua3", "ua2", "ua3"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", toProperties("k", "v"), "ua1");

                assertTrue(pap.graph().nodeExists("ua1"));
                assertTrue(pap.graph().nodeExists("ua2"));
                assertEquals("v", pap.graph().getNode("ua2").getProperties().get("k"));

                assertTrue(pap.graph().getChildren("pc1").contains("ua1"));
                assertTrue(pap.graph().getChildren("ua1").contains("ua2"));

                assertTrue(pap.graph().getParents("ua1").contains("pc1"));
                assertTrue(pap.graph().getParents("ua2").contains("ua1"));
            }
        }

        @Nested
        class CreateObjectTest {

            @Test
            void testNodeNameExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObject("o1", "oa1");
                assertThrows(NodeNameExistsException.class,
                             () -> pap.graph().createObject("o1", "oa1"));
            }

            @Test
            void testNodeDoesNotExistException() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createObject("o1", "oa1"));

                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createObjectAttribute("o1", "oa1", "oa2"));
            }

            @Test
            void testInvalidAssignmentException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                             () -> pap.graph().createObjectAttribute("o1", "ua1"));
            }

            @Test
            void testAssignmentCausesLoopException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createObject("o1", "o1"));
                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createObject("o1", "oa1", "o1"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                pap.graph().createObject("o1", toProperties("k", "v"), "oa1");

                assertTrue(pap.graph().nodeExists("o1"));
                assertEquals("v", pap.graph().getNode("o1").getProperties().get("k"));

                assertTrue(pap.graph().getChildren("oa1").contains("o1"));
                assertEquals( List.of("oa1"), pap.graph().getParents("o1"));
                assertTrue(pap.graph().getChildren("oa1").contains("o1"));
            }
        }

        @Nested
        class CreateUserTest {

            @Test
            void testNodeNameExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");
                assertThrows(NodeNameExistsException.class,
                             () -> pap.graph().createUser("u1", "ua1"));
            }

            @Test
            void testNodeDoesNotExistException() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createUser("u1", "ua1"));

                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().createUser("u1", "ua1", "ua2"));
            }

            @Test
            void testInvalidAssignmentException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                             () -> pap.graph().createUser("u1", "oa1"));
            }

            @Test
            void testAssignmentCausesLoopException()
                    throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createUser("u1", "u1"));
                assertThrows(AssignmentCausesLoopException.class,
                             () -> pap.graph().createUser("u1", "ua1", "u1"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                pap.graph().createUser("u1", toProperties("k", "v"), "ua1");

                assertTrue(pap.graph().nodeExists("u1"));
                assertEquals("v", pap.graph().getNode("u1").getProperties().get("k"));

                assertTrue(pap.graph().getChildren("ua1").contains("u1"));
                assertEquals( List.of("ua1"), pap.graph().getParents("u1"));
                assertTrue(pap.graph().getChildren("ua1").contains("u1"));
            }
        }

        @Nested
        class SetNodePropertiesTest {

            @Test
            void testNodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().setNodeProperties("oa1", NO_PROPERTIES));
            }

            @Test
            void testSuccessEmptyProperties() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().setNodeProperties("pc1", NO_PROPERTIES);

                assertTrue(pap.graph().getNode("pc1").getProperties().isEmpty());
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().setNodeProperties("pc1", toProperties("k", "v"));

                assertEquals("v", pap.graph().getNode("pc1").getProperties().get("k"));
            }
        }

        @Nested
        class NodeExists {
            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                assertTrue(pap.graph().nodeExists("pc1"));
                assertTrue(pap.graph().nodeExists("ua1"));
                assertFalse(pap.graph().nodeExists("pc2"));
            }
        }

        @Nested
        class GetNodeTest {

            @Test
            void testNodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().getNode("pc1"));
            }

            @Test
            void testSuccessPolicyClass() throws PMException {
                pap.graph().createPolicyClass("pc1", Properties.toProperties("k", "v"));

                Node pc1 = pap.graph().getNode("pc1");

                assertEquals("pc1", pc1.getName());
                assertEquals(PC, pc1.getType());
                assertEquals("v", pc1.getProperties().get("k"));
            }

            @Test
            void testSuccessObjectAttribute() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", Properties.toProperties("k", "v"), "pc1");

                Node oa1 = pap.graph().getNode("oa1");

                assertEquals("oa1", oa1.getName());
                assertEquals(OA, oa1.getType());
                assertEquals("v", oa1.getProperties().get("k"));
            }
        }

        @Nested
        class Search {
            @Test
            void testSearch() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", toProperties("namespace", "test"), "pc1");
                pap.graph().createObjectAttribute("oa2", toProperties("key1", "value1"), "pc1");
                pap.graph().createObjectAttribute("oa3", toProperties("key1", "value1", "key2", "value2"), "pc1");

                List<String> nodes = pap.graph().search(OA, NO_PROPERTIES);
                assertEquals(10, nodes.size());

                nodes = pap.graph().search(ANY, toProperties("key1", "value1"));
                assertEquals(2, nodes.size());

                nodes = pap.graph().search(ANY, toProperties("namespace", "test"));
                assertEquals(1, nodes.size());

                nodes = pap.graph().search(OA, toProperties("namespace", "test"));
                assertEquals(1, nodes.size());
                nodes = pap.graph().search(OA, toProperties("key1", "value1"));
                assertEquals(2, nodes.size());
                nodes = pap.graph().search(OA, toProperties("key1", "*"));
                assertEquals(2, nodes.size());
                nodes = pap.graph().search(OA, toProperties("key1", "value1", "key2", "value2"));
                assertEquals(1, nodes.size());
                nodes = pap.graph().search(OA, toProperties("key1", "value1", "key2", "*"));
                assertEquals(1, nodes.size());
                nodes = pap.graph().search(OA, toProperties("key1", "value1", "key2", "no_value"));
                assertEquals(0, nodes.size());
                nodes = pap.graph().search(ANY, NO_PROPERTIES);
                assertEquals(12, nodes.size());
            }
        }

        @Nested
        class GetPolicyClasses {
            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createPolicyClass("pc2");
                pap.graph().createPolicyClass("pc3");

                assertTrue(pap.graph().getPolicyClasses().containsAll(Arrays.asList("pc1", "pc2", "pc3")));
            }
        }

        @Nested
        class DeleteNodeTest {

            @Test
            void testNodeDoesNotExistDoesNotThrowException() {
                assertDoesNotThrow(() -> pap.graph().deleteNode("pc1"));
            }

            @Test
            void testNodeHasChildrenException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeHasChildrenException.class,
                             () -> pap.graph().deleteNode("pc1"));
            }

            @Test
            void DeleteNodeWithProhibitionsAndObligations() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "pc1");
                pap.graph().createUser("u1", "ua2");
                pap.graph().createUserAttribute("oa1", "pc1");
                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("ua1"),
                                          new AccessRightSet(), true, new ContainerCondition("oa1", true));

                assertThrows(NodeReferencedInProhibitionException.class,
                             () -> pap.graph().deleteNode("ua1"));
                assertThrows(NodeReferencedInProhibitionException.class,
                             () -> pap.graph().deleteNode("oa1"));

                pap.prohibitions().delete("pro1");
                pap.obligations().create(new UserContext("u1"), "oblLabel",
                                         new Rule(
                                                 "rule1",
                                                 new EventPattern(
                                                         EventSubject.anyUserWithAttribute("ua1"),
                                                         new Performs("event1")
                                                 ),
                                                 new Response(new UserContext(""))
                                         ),
                                         new Rule(
                                                 "rule1",
                                                 new EventPattern(
                                                         EventSubject.users("ua1"),
                                                         new Performs("event1")
                                                 ),
                                                 new Response(new UserContext(""))
                                         )
                );

                assertThrows(NodeReferencedInObligationException.class,
                             () -> pap.graph().deleteNode("ua1"));
            }

            @Test
            void testSuccessPolicyClass() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().deleteNode("pc1");
                assertFalse(pap.graph().nodeExists("pc1"));
                assertFalse(pap.graph().nodeExists(AdminPolicy.policyClassTargetName("pc1")));
            }

            @Test
            void testSuccessObjectAttribute() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                pap.graph().deleteNode("oa1");

                assertFalse(pap.graph().nodeExists("oa1"));
            }
        }

        @Nested
        class AssignTest {

            @Test
            void testChildNodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().assign("oa1", "pc1"));
            }

            @Test
            void testParentNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().assign("oa1", "oa2"));
            }

            @Test
            void testAssignmentExistsDoesNothing() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertDoesNotThrow(() -> pap.graph().assign("oa1", "pc1"));
            }

            @Test
            void testInvalidAssignmentException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                             () -> pap.graph().assign("ua1", "oa1"));
            }

            @Test
            void testAssignmentCausesLoopException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "oa1");
                pap.graph().createObjectAttribute("oa3", "oa2");

                assertThrows(AssignmentCausesLoopException.class, () ->
                        pap.graph().assign("oa1", "oa2"));
                assertThrows(AssignmentCausesLoopException.class, () ->
                        pap.graph().assign("oa1", "oa1"));
                assertThrows(AssignmentCausesLoopException.class, () ->
                        pap.graph().assign("oa1", "oa3"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().assign("oa2", "oa1");
                assertTrue(pap.graph().getParents("oa2").contains("oa1"));
                assertTrue(pap.graph().getChildren("oa1").contains("oa2"));
            }
        }

        @Nested
        class DeassignTest {

            @Test
            void testChildNodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class, () ->
                        pap.graph().deassign("oa1", "pc1"));
            }

            @Test
            void testParentNodeDoesNotExistException() throws PMException{
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeDoesNotExistException.class, () ->
                        pap.graph().deassign("oa1", "oa2"));
            }

            @Test
            void AssignmentDoesNotExistDoesNothing() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().deassign("oa1", "oa2");
            }

            @Test
            void testDisconnectedNode() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(DisconnectedNodeException.class,
                             () -> pap.graph().deassign("oa1", "pc1"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createPolicyClass("pc2");
                pap.graph().createObjectAttribute("oa1", "pc1", "pc2");
                pap.graph().deassign("oa1", "pc1");
                assertEquals(List.of("pc2"), pap.graph().getParents("oa1"));
                assertFalse(pap.graph().getParents("oa1").contains("pc1"));
                assertFalse(pap.graph().getChildren("pc1").contains("oa1"));
            }

        }

        @Nested
        class GetChildrenTest {

            @Test
            void NodeDoesNotExist() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().getChildren("oa1"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");


                assertTrue(pap.graph().getChildren("pc1").containsAll(List.of("oa1", "oa2", "oa3")));
            }
        }

        @Nested
        class GetParentsTest {

            @Test
            void NodeDoesNotExist() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().getParents("oa1"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObject("o1", "oa1");
                pap.graph().assign("o1", "oa2");
                pap.graph().assign("o1", "oa3");

                assertTrue(pap.graph().getParents("o1").containsAll(List.of("oa1", "oa2", "oa3")));
            }
        }

        @Nested
        class AssociateTest {

            @Test
            void testUANodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().associate("ua1", "oa1", new AccessRightSet()));
            }

            @Test
            void testTargetNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().associate("ua1", "oa1", new AccessRightSet()));
            }

            @Test
            void testAssignmentExistsDoesNotThrowException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "ua1");
                assertDoesNotThrow(() -> pap.graph().associate("ua2", "ua1", new AccessRightSet()));
            }

            @Test
            void testUnknownAccessRightException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(UnknownAccessRightException.class,
                             () -> pap.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                pap.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(UnknownAccessRightException.class,
                             () -> pap.graph().associate("ua1", "oa1", new AccessRightSet("write")));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet(ALL_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS)));
            }

            @Test
            void testInvalidAssociationException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "ua1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");

                assertThrows(InvalidAssociationException.class,
                             () -> pap.graph().associate("ua2", "pc1", new AccessRightSet()));
                assertThrows(InvalidAssociationException.class,
                             () -> pap.graph().associate("oa1", "oa2", new AccessRightSet()));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));

                assertTrue(
                        pap.graph().getAssociationsWithSource("ua1")
                           .contains(new Association("ua1", "oa1"))
                );
                assertTrue(
                        pap.graph().getAssociationsWithTarget("oa1")
                           .contains(new Association("ua1", "oa1"))
                );
            }

            @Test
            void testOverwriteSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));

                List<Association> assocs = pap.graph().getAssociationsWithSource("ua1");
                Association assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read"), assoc.getAccessRightSet());

                pap.graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));

                assocs = pap.graph().getAssociationsWithSource("ua1");
                assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read", "write"), assoc.getAccessRightSet());
            }
        }

        @Nested
        class DissociateTest {

            @Test
            void testUANodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().dissociate("ua1", "oa1"));
            }

            @Test
            void testTargetNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().dissociate("ua1", "oa2"));
            }

            @Test
            void testAssociationDoesNotExistDoesNotThrowException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> pap.graph().dissociate("ua1", "oa1"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet());

                pap.graph().dissociate("ua1", "oa1");

                assertFalse(pap.graph().getAssociationsWithSource("ua1")
                               .contains(new Association("ua1", "oa1")));
                assertFalse(pap.graph().getAssociationsWithTarget("oa1")
                               .contains(new Association("ua1", "oa1")));
            }
        }

        @Nested
        class GetAssociationsWithSourceTest {

            @Test
            void testNodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().getAssociationsWithSource("ua1"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                pap.graph().associate("ua1", "oa2", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.graph().getAssociationsWithSource("ua1");

                assertEquals(2, assocs.size());

                for (Association assoc : assocs) {
                    checkAssociation(assoc);
                }
            }

            private void checkAssociation(Association association) {
                if (association.getTarget().equals("oa1")) {
                    assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
                } else if (association.getTarget().equals("oa2")) {
                    assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
                }
            }
        }

        @Nested
        class GetAssociationsWithTargetTest {

            @Test
            void testNodeDoesNotExistException() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.graph().getAssociationsWithTarget("oa1"));
            }

            @Test
            void Success() throws PMException {
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                pap.graph().associate("ua2", "oa1", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.graph().getAssociationsWithTarget("oa1");

                assertEquals(2, assocs.size());

                for (Association assoc : assocs) {
                    checkAssociation(assoc);
                }
            }

            private void checkAssociation(Association association) {
                if (association.getSource().equals("ua1")) {
                    assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
                } else if (association.getSource().equals("ua2")) {
                    assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
                }
            }
        }
    }

    @Nested
    class ProhibitionsStoreTests {
        @Nested
        class CreateProhibitionTest {

            @Test
            void testProhibitionExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false);

                assertThrows(ProhibitionExistsException.class,
                             () -> pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false));
            }

            @Test
            void testProhibitionSubjectDoesNotExistException() {
                assertThrows(ProhibitionSubjectDoesNotExistException.class,
                             () -> pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS), false));
            }


            @Test
            void testUnknownAccessRightException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");

                assertThrows(UnknownAccessRightException.class,
                             () -> pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false));
            }

            @Test
            void testProhibitionContainerDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(ProhibitionContainerDoesNotExistException.class,
                             () -> pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa1", true)));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true),
                                          new ContainerCondition("oa2", false));

                Prohibition p = pap.prohibitions().get("pro1");
                assertEquals("pro1", p.getName());
                assertEquals("subject", p.getSubject().getName());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            }
        }

        @Nested
        class UpdateProhibitionTest {

            @Test
            void testProhibitionDoesNotExistException() {
                assertThrows(ProhibitionDoesNotExistException.class,
                             () -> pap.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("test"), false));
            }


            @Test
            void testUnknownAccessRightException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true));

                assertThrows(UnknownAccessRightException.class,
                             () -> pap.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("test"), false));
            }

            @Test
            void testProhibitionSubjectDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true));

                assertThrows(ProhibitionSubjectDoesNotExistException.class,
                             () -> pap.prohibitions().update("pro1", ProhibitionSubject.userAttribute("test"), new AccessRightSet("read"), false));
                assertDoesNotThrow(() -> pap.prohibitions().update("pro1", ProhibitionSubject.process("subject"), new AccessRightSet("read"), false));
            }

            @Test
            void testProhibitionContainerDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true));

                assertThrows(ProhibitionContainerDoesNotExistException.class,
                             () -> pap.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa3", true)));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createUserAttribute("subject2", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true),
                                          new ContainerCondition("oa2", false));
                pap.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read", "write"), true,
                                          new ContainerCondition("oa1", false),
                                          new ContainerCondition("oa2", true));

                Prohibition p = pap.prohibitions().get("pro1");
                assertEquals("pro1", p.getName());
                assertEquals("subject2", p.getSubject().getName());
                assertEquals(new AccessRightSet("read", "write"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true)
                ), p.getContainers());
            }
        }

        @Nested
        class DeleteProhibitionTest {

            @Test
            void testNonExistingProhibitionDoesNotThrowException() {
                assertDoesNotThrow(() -> pap.prohibitions().delete("pro1"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true),
                                          new ContainerCondition("oa2", false));

                assertDoesNotThrow(() -> pap.prohibitions().get("pro1"));

                pap.prohibitions().delete("pro1");

                assertThrows(ProhibitionDoesNotExistException.class,
                             () -> pap.prohibitions().get("pro1"));
            }
        }

        @Nested
        class GetAll {

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObjectAttribute("oa4", "pc1");

                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true),
                                          new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa3", true),
                                          new ContainerCondition("oa4", false));

                Map<String, List<Prohibition>> prohibitions = pap.prohibitions().getAll();
                assertEquals(1, prohibitions.size());
                assertEquals(2, prohibitions.get("subject").size());
                checkProhibitions(prohibitions.get("subject"));
            }

            private void checkProhibitions(List<Prohibition> prohibitions) {
                for (Prohibition p : prohibitions) {
                    if (p.getName().equals("label1")) {
                        assertEquals("label1", p.getName());
                        assertEquals("subject", p.getSubject().getName());
                        assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                        assertTrue(p.isIntersection());
                        assertEquals(2, p.getContainers().size());
                        assertEquals(List.of(
                                new ContainerCondition("oa1", true),
                                new ContainerCondition("oa2", false)
                        ), p.getContainers());
                    } else if (p.getName().equals("label2")) {
                        assertEquals("label2", p.getName());
                        assertEquals("subject", p.getSubject().getName());
                        assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                        assertTrue(p.isIntersection());
                        assertEquals(2, p.getContainers().size());
                        assertEquals(List.of(
                                new ContainerCondition("oa3", true),
                                new ContainerCondition("oa4", false)
                        ), p.getContainers());
                    } else {
                        fail("unexpected prohibition label " + p.getName());
                    }
                }
            }
        }

        @Nested
        class GetWithSubject {

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject1", "pc1");
                pap.graph().createUserAttribute("subject2", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObjectAttribute("oa4", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject1"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true),
                                          new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa3", true),
                                          new ContainerCondition("oa4", false));

                List<Prohibition> pros = pap.prohibitions().getWithSubject("subject1");
                assertEquals(1, pros.size());

                Prohibition p = pros.get(0);

                assertEquals("label1", p.getName());
                assertEquals("subject1", p.getSubject().getName());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            }

        }

        @Nested
        class Get {

            @Test
            void testSuccess() throws PMException {
                assertThrows(ProhibitionDoesNotExistException.class,
                             () -> pap.prohibitions().get("pro1"));

                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObjectAttribute("oa4", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa1", true),
                                          new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                                          new ContainerCondition("oa3", true),
                                          new ContainerCondition("oa4", false));

                Prohibition p = pap.prohibitions().get("label1");
                assertEquals("label1", p.getName());
                assertEquals("subject", p.getSubject().getName());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            }
        }
    }

    @Nested
    class ObligationsStoreTests {

        Obligation obligation1 = new Obligation(
                new UserContext("u1"),
                "obl1",
                List.of(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext("u1"),
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                                )
                        )
                )
        );

        Obligation obligation2 = new Obligation(
                new UserContext("u1"),
                "label2")
                .addRule(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext("u1"),
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                                )
                        )
                ).addRule(
                        new Rule(
                                "rule2",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext("u1"),
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                                )
                        )
                );


        @Nested
        class CreateObligation {

            @Test
            void testObligationNameExistsException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(ObligationNameExistsException.class, () -> pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new)));
            }

            @Test
            void testAuthorNodeDoestNotExistException() {
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().create(new UserContext("u1"), obligation1.getName(),
                                                            obligation1.getRules().toArray(Rule[]::new)));
            }

            @Test
            void testEventSubjectNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().create(
                                     new UserContext("u1"),
                                     "obl1",
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("ua2"),
                                                     Performs.events("test_event"),
                                                     Target.anyPolicyElement()
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().create(
                                     new UserContext("u1"),
                                     "obl1",
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.anyUserWithAttribute("ua3"),
                                                     Performs.events("test_event"),
                                                     Target.anyPolicyElement()
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
            }

            @Test
            void testEventTargetNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().create(
                                     new UserContext("u1"),
                                     "obl1",
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("u1"),
                                                     Performs.events("test_event"),
                                                     Target.anyOfSet("oa1")
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().create(
                                     new UserContext("u1"),
                                     "obl1",
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("u1"),
                                                     Performs.events("test_event"),
                                                     Target.policyElement("oa1")
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().create(
                                     new UserContext("u1"),
                                     "obl1",
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("u1"),
                                                     Performs.events("test_event"),
                                                     Target.anyContainedIn("oa1")
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(ObligationNameExistsException.class,
                             () -> pap.obligations().create(obligation1.getAuthor(), obligation1.getName()));

                Obligation actual = pap.obligations().get(obligation1.getName());
                assertEquals(obligation1, actual);
            }
        }

        @Nested
        class UpdateObligation {

            @Test
            void testObligationDoesNotExistException() {
                assertThrows(ObligationDoesNotExistException.class, () -> pap.obligations().update(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new)));
            }

            @Test
            void testAuthorNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().update(new UserContext("u2"), obligation1.getName(),
                                                            obligation1.getRules().toArray(Rule[]::new)));
            }

            @Test
            void testEventSubjectNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().update(
                                     new UserContext("u1"),
                                     obligation1.getName(),
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("ua2"),
                                                     Performs.events("test_event"),
                                                     Target.anyPolicyElement()
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().update(
                                     new UserContext("u1"),
                                     obligation1.getName(),
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.anyUserWithAttribute("ua2"),
                                                     Performs.events("test_event"),
                                                     Target.anyPolicyElement()
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
            }

            @Test
            void testEventTargetNodeDoesNotExistException() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().update(
                                     new UserContext("u1"),
                                     obligation1.getName(),
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("u1"),
                                                     Performs.events("test_event"),
                                                     Target.anyOfSet("oa1")
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().update(
                                     new UserContext("u1"),
                                     obligation1.getName(),
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("u1"),
                                                     Performs.events("test_event"),
                                                     Target.policyElement("oa1")
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
                assertThrows(NodeDoesNotExistException.class,
                             () -> pap.obligations().update(
                                     new UserContext("u1"),
                                     obligation1.getName(),
                                     new Rule(
                                             "rule1",
                                             new EventPattern(
                                                     EventSubject.users("u1"),
                                                     Performs.events("test_event"),
                                                     Target.anyContainedIn("oa1")
                                             ),
                                             new Response(new UserContext("u1"))
                                     )
                             ));
            }

            @Test
            void testSuccess() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                assertThrows(ObligationDoesNotExistException.class,
                             () -> pap.obligations().update(new UserContext("u1"), obligation1.getName()));

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                pap.obligations().update(new UserContext("u1"), obligation1.getName(),
                                         obligation2.getRules().toArray(Rule[]::new));

                Obligation expected = new Obligation(obligation1);
                expected.setRules(obligation2.getRules());

                Obligation actual = pap.obligations().get(obligation1.getName());
                assertEquals(expected, actual);
            }

        }

        @Nested
        class DeleteNode {

            @Test
            void testDeleteNonExistingObligationDoesNOtThrowExcpetion() {
                assertDoesNotThrow(() -> pap.obligations().delete(obligation1.getName()));
            }

            @Test
            void testDeleteObligation() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));
                pap.obligations().create(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules().toArray(Rule[]::new));

                pap.obligations().delete(obligation1.getName());

                assertThrows(ObligationDoesNotExistException.class,
                             () -> pap.obligations().get(obligation1.getName()));
            }
        }


        @Nested
        class GetAll {
            @Test
            void testGetObligations() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));
                pap.obligations().create(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules().toArray(Rule[]::new));

                List<Obligation> obligations = pap.obligations().getAll();
                assertEquals(2, obligations.size());
                for (Obligation obligation : obligations) {
                    if (obligation.getName().equals(obligation1.getName())) {
                        assertEquals(obligation1, obligation);
                    } else {
                        assertEquals(obligation2, obligation);
                    }
                }
            }
        }


        @Nested
        class Get {

            @Test
            void testObligationDoesNotExistException() {
                assertThrows(ObligationDoesNotExistException.class,
                             () -> pap.obligations().get(obligation1.getName()));
            }

            @Test
            void testGetObligation() throws PMException {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUser("u1", "ua1");

                pap.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));
                pap.obligations().create(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules().toArray(Rule[]::new));

                Obligation obligation = pap.obligations().get(obligation1.getName());
                assertEquals(obligation1, obligation);
            }
        }
    }

    @Nested
    class UserDefinedPMLStoreTests {

        @Nested
        class CreateFunction {

            FunctionDefinitionStatement testFunc = new FunctionDefinitionStatement(
                    "testFunc",
                    Type.string(),
                    Arrays.asList(
                            new FormalArgument("arg1", Type.string()),
                            new FormalArgument("arg2", Type.array(Type.string()))
                    ),
                    Arrays.asList(
                            new CreatePolicyStatement(new Expression(new VariableReference("pc1", Type.string()))),
                            new CreateAttrStatement(
                                    new Expression(new VariableReference("ua1", Type.string())),
                                    UA,
                                    new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new VariableReference("pc1", Type.string()))}, Type.string())))
                            ),
                            new CreateAttrStatement(
                                    new Expression(new VariableReference("oa1", Type.string())),
                                    OA,
                                    new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new VariableReference("pc1", Type.string()))}, Type.string())))
                            )
                    )
            );

            @Test
            void testPMLFunctionAlreadyDefinedException() throws PMException {
                pap.userDefinedPML().createFunction(testFunc);
                assertThrows(PMLFunctionAlreadyDefinedException.class, () -> pap.userDefinedPML().createFunction(testFunc));
            }

            @Test
            void testSuccess() throws PMException {
                pap.userDefinedPML().createFunction(testFunc);
                assertTrue(pap.userDefinedPML().getFunctions().containsKey(testFunc.getFunctionName()));
                FunctionDefinitionStatement actual = pap.userDefinedPML().getFunctions().get(testFunc.getFunctionName());
                assertEquals(testFunc, actual);
            }
        }

        @Nested
        class DeleteFunction {

            @Test
            void testNonExistingFunctionDoesNotThrowException() {
                assertDoesNotThrow(() -> pap.userDefinedPML().deleteFunction("func"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.userDefinedPML().createFunction(new FunctionDefinitionStatement("testFunc", Type.voidType(), List.of(), List.of()));
                assertTrue(pap.userDefinedPML().getFunctions().containsKey("testFunc"));
                pap.userDefinedPML().deleteFunction("testFunc");
                assertFalse(pap.userDefinedPML().getFunctions().containsKey("testFunc"));
            }
        }

        @Nested
        class GetFunctions {

            @Test
            void testSuccess() throws PMException {
                FunctionDefinitionStatement testFunc1 = new FunctionDefinitionStatement("testFunc1", Type.voidType(), List.of(), List.of());
                FunctionDefinitionStatement testFunc2 = new FunctionDefinitionStatement("testFunc2", Type.voidType(), List.of(), List.of());

                pap.userDefinedPML().createFunction(testFunc1);
                pap.userDefinedPML().createFunction(testFunc2);

                Map<String, FunctionDefinitionStatement> functions = pap.userDefinedPML().getFunctions();
                assertTrue(functions.containsKey("testFunc1"));
                FunctionDefinitionStatement actual = functions.get("testFunc1");
                assertEquals(testFunc1, actual);

                assertTrue(functions.containsKey("testFunc2"));
                actual = functions.get("testFunc2");
                assertEquals(testFunc2, actual);
            }

        }

        @Nested
        class GetFunction {

            @Test
            void testPMLFunctionNotDefinedException() {
                assertThrows(PMLFunctionNotDefinedException.class, () -> pap.userDefinedPML().getFunction("func1"));
            }

            @Test
            void testSuccess() throws PMException {
                FunctionDefinitionStatement testFunc1 = new FunctionDefinitionStatement("testFunc1", Type.voidType(), List.of(), List.of());
                FunctionDefinitionStatement testFunc2 = new FunctionDefinitionStatement("testFunc2", Type.voidType(), List.of(), List.of());

                pap.userDefinedPML().createFunction(testFunc1);
                pap.userDefinedPML().createFunction(testFunc2);

                Map<String, FunctionDefinitionStatement> functions = pap.userDefinedPML().getFunctions();
                assertTrue(functions.containsKey("testFunc1"));
                FunctionDefinitionStatement actual = functions.get("testFunc1");
                assertEquals(testFunc1, actual);

                assertTrue(functions.containsKey("testFunc2"));
                actual = functions.get("testFunc2");
                assertEquals(testFunc2, actual);
            }

        }

        @Nested
        class CreateConstant {

            @Test
            void testPMLConstantAlreadyDefinedException() throws PMException {
                pap.userDefinedPML().createConstant("const1", new Value("test"));
                assertThrows(PMLConstantAlreadyDefinedException.class, () -> pap.userDefinedPML().createConstant("const1", new Value("test")));
            }

            @Test
            void testSuccess() throws PMException {
                Value expected = new Value("test");

                pap.userDefinedPML().createConstant("const1", expected);
                assertTrue(pap.userDefinedPML().getConstants().containsKey("const1"));
                Value actual = pap.userDefinedPML().getConstants().get("const1");
                assertEquals(expected, actual);
            }
        }

        @Nested
        class DeleteConstant {

            @Test
            void testNonExistingConstantDoesNotThrowException() {
                assertDoesNotThrow(() -> pap.userDefinedPML().deleteConstant("const1"));
            }

            @Test
            void testSuccess() throws PMException {
                pap.userDefinedPML().createConstant("const1", new Value("test"));
                assertTrue(pap.userDefinedPML().getConstants().containsKey("const1"));
                pap.userDefinedPML().deleteConstant("const1");
                assertFalse(pap.userDefinedPML().getConstants().containsKey("const1"));
            }
        }

        @Nested
        class GetConstants {

            @Test
            void success() throws PMException {
                Value const1 = new Value("test1");
                Value const2 = new Value("test2");

                pap.userDefinedPML().createConstant("const1", const1);
                pap.userDefinedPML().createConstant("const2", const2);

                Map<String, Value> constants = pap.userDefinedPML().getConstants();
                assertTrue(constants.containsKey("const1"));
                Value actual = constants.get("const1");
                assertEquals(const1, actual);

                assertTrue(constants.containsKey("const2"));
                actual = constants.get("const2");
                assertEquals(const2, actual);
            }
        }


        @Nested
        class GetConstant {

            @Test
            void testPMLConstantNotDefinedException() {
                assertThrows(PMLConstantNotDefinedException.class, () -> pap.userDefinedPML().getConstant("const1"));
            }

            @Test
            void success() throws PMException {
                Value const1 = new Value("test1");
                Value const2 = new Value("test2");

                pap.userDefinedPML().createConstant("const1", const1);
                pap.userDefinedPML().createConstant("const2", const2);

                Map<String, Value> constants = pap.userDefinedPML().getConstants();
                assertTrue(constants.containsKey("const1"));
                Value actual = constants.get("const1");
                assertEquals(const1, actual);

                assertTrue(constants.containsKey("const2"));
                actual = constants.get("const2");
                assertEquals(const2, actual);
            }
        }

    }
}