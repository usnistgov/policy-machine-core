package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.policy.exceptions.PMException;
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

class MysqlGraphStoreTest {

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
    void testRollbackTx() throws PMException, SQLException {
        MysqlConnection connection
                = new MysqlConnection(DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword()));
        MysqlPolicyStore mysqlPolicyStore = new MysqlPolicyStore(connection);

        MysqlGraphStore graph = mysqlPolicyStore.graph();
        graph.createPolicyClass("pc1", noprops());

        // put a row in the assignment table for oa1 -> pc1
        // this should cause an error and a rollback
        try(Statement stmt = connection.getConnection().createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            stmt.executeUpdate("insert into assignment values (1, 2, 1)");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
        }

        assertThrows(MysqlPolicyException.class, () -> graph.createObjectAttribute("oa1", noprops(), "pc1"));
        assertFalse(graph.nodeExists("oa1"));
    }

}