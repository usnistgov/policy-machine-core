package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTest;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class MysqlPAPTest extends PAPTest {

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
        connection.close();
        testEnv.reset();
    }

    private Connection connection;

    @Override
    public PAP getPAP() throws PMException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new PMException(e);
            }
        }

        try {
            connection = DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword());
        } catch (SQLException e) {
            throw new PMException(e);
        }

        return new PAP(new MysqlPolicyStore(connection));
    }



    @Test
    void testTx() throws SQLException, PMException {
        try (Connection connection = DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword());
             Connection connection2 = DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword())) {

            PAP pap = new PAP(new MysqlPolicyStore(connection));
            pap.beginTx();
            pap.graph().createPolicyClass("pc1");
            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.commit();


            PAP pap2 = new PAP(new MysqlPolicyStore(connection2));
            assertTrue(pap2.graph().nodeExists("pc1"));
            assertTrue(pap2.graph().nodeExists("oa1"));
        }
    }

    @Test
    void testRollbackProhibitionTx() throws PMException, SQLException {
        Connection connection
                = DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword());
        MysqlPolicyStore mysqlPolicyStore = new MysqlPolicyStore(connection);

        PAP pap = new PAP(mysqlPolicyStore);
        pap.graph().createPolicyClass("pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");

        try(Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            stmt.executeUpdate("insert into prohibition_container values (1, (select id from node where name = 'oa1'), 1)");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
        }

        assertThrows(MysqlPolicyException.class, () ->
                pap.prohibitions().create("pro1", ProhibitionSubject.userAttribute("ua1"),
                        new AccessRightSet(), false, new ContainerCondition("oa1", true)));
        assertThrows(ProhibitionDoesNotExistException.class, () -> pap.prohibitions().get("pro1"));
    }

}