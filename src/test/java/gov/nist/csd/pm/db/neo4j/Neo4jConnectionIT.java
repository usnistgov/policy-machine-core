package gov.nist.csd.pm.db.neo4j;

import gov.nist.csd.pm.db.DatabaseContext;
import gov.nist.csd.pm.exceptions.PMDBException;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Neo4jConnectionIT {

    @Test
    void testGetConnection() throws PMDBException, IOException {
        DatabaseContext dbCtx = TestUtils.getDatabaseContext();
        Neo4jConnection connection = new Neo4jConnection(dbCtx.getHost(), dbCtx.getPort(), dbCtx.getUsername(), dbCtx.getPassword());
        connection.getConnection();

        assertThrows(PMDBException.class, () -> new Neo4jConnection("", 0, "", "").getConnection());
    }
}