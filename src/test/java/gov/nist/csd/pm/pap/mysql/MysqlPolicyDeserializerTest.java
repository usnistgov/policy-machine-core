package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.pap.memory.MemoryPolicyDeserializer;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.PolicyEquals;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MysqlPolicyDeserializerTest {

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
    void testDeserialize() throws PMException, IOException, SQLException {
        try (Connection connection = DriverManager.getConnection(testEnv.getConnectionUrl(), testEnv.getUser(), testEnv.getPassword())) {
            MysqlPolicyStore mysqlPolicyStore = new MysqlPolicyStore(connection);
            SamplePolicy.loadSamplePolicyFromPML(mysqlPolicyStore);

            String json = mysqlPolicyStore.serialize().toJSON();

            MemoryPolicyStore actual = new MemoryPolicyStore();
            new MemoryPolicyDeserializer(actual).fromJSON(json);

            PolicyEquals.check(mysqlPolicyStore, actual);
        }
    }

}