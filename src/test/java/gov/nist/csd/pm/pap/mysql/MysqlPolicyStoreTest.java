package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.pap.PolicyStoreTest;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlPolicyStoreTest extends PolicyStoreTest {

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
    public PolicyStore getPolicyStore() throws PMException {
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

        return new MysqlPolicyStore(connection);
    }
}
