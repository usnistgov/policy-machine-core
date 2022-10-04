package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.*;

class MysqlProhibitionsStoreTest {

    static MysqlTestEnv testEnv;

    @BeforeAll
    static void start() throws PMException, IOException {
        testEnv = new MysqlTestEnv();
        testEnv.start();
    }

    @AfterAll
    static void stop() {
        testEnv.stop();
    }

    @AfterEach
    void reset() throws SQLException {
        testEnv.reset();
    }

    @Test
    void testRollbackTx() throws PMException, IOException, SQLException {
        MysqlConnection connection
                = new MysqlConnection(DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword()));
        MysqlPolicyStore mysqlPolicyStore = new MysqlPolicyStore(connection);

        MysqlGraphStore graph = mysqlPolicyStore.graph();
        graph.createPolicyClass("pc1");
        graph.createUserAttribute("ua1", "pc1");
        graph.createObjectAttribute("oa1", "pc1");

        try(Statement stmt = connection.getConnection().createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            stmt.executeUpdate("insert into prohibition_container values (1, 3, 1)");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
        }

        MysqlProhibitionsStore prohibitions = mysqlPolicyStore.prohibitions();
        assertThrows(MysqlPolicyException.class, () -> prohibitions.create("label", ProhibitionSubject.userAttribute("ua1"),
                new AccessRightSet(), false, new ContainerCondition("oa1", true)));
        assertThrows(ProhibitionDoesNotExistException.class, () -> prohibitions.get("label"));
    }

}