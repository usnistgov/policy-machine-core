package gov.nist.csd.pm.core.impl.neo4j.embedded.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jEmbeddedPolicyStore;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTest;
import gov.nist.csd.pm.core.pap.modification.*;
import gov.nist.csd.pm.core.pap.query.AccessQuerierTest;
import gov.nist.csd.pm.core.pap.query.GraphQuerierTest;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerierTest;
import gov.nist.csd.pm.core.pap.query.OperationsQuerierTest;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuerierTest;
import gov.nist.csd.pm.core.pap.query.RoutinesQuerierTest;
import gov.nist.csd.pm.core.util.TestIdGenerator;
import java.nio.file.Path;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.Neo4jTestInitializer.init;
import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

class Neo4jTestInitializer {
	
	private static GraphDatabaseService graphDb;
	
	public static GraphDatabaseService getTx(Path tempDir) {
        if (graphDb == null) {
			DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(tempDir).build();
            graphDb = managementService.database(DEFAULT_DATABASE_NAME);
			Neo4jEmbeddedPolicyStore.createIndexes(graphDb);
		}

		try(Transaction tx = graphDb.beginTx()) {
			tx.execute("match (n) detach delete n");
			tx.commit();
		}
		
		return graphDb;
	}

	public static PAP init(Path tempDir) throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(Neo4jTestInitializer.getTx(tempDir)))
			.withIdGenerator(new TestIdGenerator());
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4JEmbeddedPAPTest extends PAPTest {
	
	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedAccessQuerierTest extends AccessQuerierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedGraphModifierTest extends GraphModifierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedGraphQuerierTest extends GraphQuerierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedObligationsModifierTest extends ObligationsModifierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedObligationsQuerierTest extends ObligationsQuerierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}

}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedOperationsModifierTest extends OperationsModifierTest {
	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedOperationsQueryTest extends OperationsQuerierTest {
	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedProhibitionsModifierTest extends ProhibitionsModifierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedProhibitionsQuerierTest extends ProhibitionsQuerierTest {

	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedRoutinesModifierTest extends RoutinesModifierTest {
	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Neo4jEmbeddedRoutinesQuerierTest extends RoutinesQuerierTest {
	@TempDir
	private Path tempDir;
	
	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}
