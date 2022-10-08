package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlPAP;
import gov.nist.csd.pm.pap.mysql.MysqlTestEnv;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryPDPTest {

    private static MysqlTestEnv testEnv;

    @BeforeAll
    public static void start() throws IOException, PMException {
        testEnv = new MysqlTestEnv();
        testEnv.start();
    }

    @AfterAll
    public static void stop() {
        testEnv.stop();
    }

    @AfterEach
    void reset() throws SQLException {
        testEnv.reset();
    }

    @Test
    void testTx() throws PMException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        MemoryPDP pdp = new MemoryPDP(new MemoryPAP(memoryPolicyStore));

        PAP genericPAP  = new MemoryPAP(memoryPolicyStore);
        MemoryPDP pdpWithGenericPAP = new MemoryPDP(genericPAP);

        pdp.runTx(new UserContext(SUPER_USER), policy -> {
            policy.graph().createPolicyClass("pc1");
            policy.graph().createUserAttribute("ua1", "pc1");
            assertTrue(pdp.policyReviewer().isContained("ua1", "pc1"));
            assertFalse(memoryPolicyStore.graph().nodeExists("pc1"));
            assertFalse(genericPAP.graph().nodeExists("pc1"));
            assertThrows(NodeDoesNotExistException.class, () -> pdpWithGenericPAP.policyReviewer().isContained("ua1", "pc1"));
        });

        assertTrue(pdp.policyReviewer().isContained("ua1", "pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));

        try (Connection connection = testEnv.getConnection()) {
            PAP mysqlPAP = new MysqlPAP(connection);

            PDP pdpWithMysqlPAP = new MemoryPDP(mysqlPAP);

            pdpWithMysqlPAP.runTx(new UserContext(SUPER_USER), policy -> {
                policy.graph().createPolicyClass("pc1");
                policy.graph().createUserAttribute("ua1", "pc1");
                try (Connection connection2 = testEnv.getConnection()) {
                    PAP mysqlPAP2 = new MysqlPAP(connection2);
                    assertFalse(mysqlPAP2.graph().nodeExists("pc1"));
                } catch (SQLException e) {
                    fail(e.getMessage());
                }
            });

            assertTrue(pdpWithMysqlPAP.policyReviewer().isContained("ua1", "pc1"));
            assertTrue(mysqlPAP.graph().nodeExists("pc1"));
            assertTrue(mysqlPAP.graph().nodeExists("ua1"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRollback() throws PMException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        memoryPolicyStore.graph().createPolicyClass("pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");

        MemoryPDP pdp = new MemoryPDP(new MemoryPAP(memoryPolicyStore));
        assertThrows(NodeNameExistsException.class, () -> {
            pdp.runTx(new UserContext(SUPER_USER), policy -> {
                policy.graph().createPolicyClass("pc2");
                // expect error and rollback
                policy.graph().createObjectAttribute("oa1", "pc2");
            });
        });

        assertEquals(10, memoryPolicyStore.graph().search(NodeType.ANY, noprops()).size());
        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("ua1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("oa1"));
        assertFalse(memoryPolicyStore.graph().nodeExists("pc2"));
    }
}
