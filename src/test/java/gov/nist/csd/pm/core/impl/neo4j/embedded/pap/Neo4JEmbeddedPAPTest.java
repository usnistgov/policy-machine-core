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
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.io.File;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.TestTx.init;
import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

class TestTx {
	
	private static GraphDatabaseService graphDb;
	
	public static GraphDatabaseService getTx() {
        if (graphDb == null) {
			DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(new File("/tmp/test").toPath()).build();
			graphDb = managementService.database(DEFAULT_DATABASE_NAME);
			Neo4jEmbeddedPolicyStore.createIndexes(graphDb);
		}

		try(Transaction tx = graphDb.beginTx()) {
			tx.execute("match (n) detach delete n");
			tx.commit();
		}
		
		return graphDb;
	}

	public static PAP init() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
			.withIdGenerator(new TestIdGenerator());
	}

	public static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

public class Neo4JEmbeddedPAPTest extends PAPTest {
	
	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedAccessQuerierTest extends AccessQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}
}

class Neo4jEmbeddedGraphModifierTest extends GraphModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedGraphQuerierTest extends GraphQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedObligationsModifierTest extends ObligationsModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedObligationsQuerierTest extends ObligationsQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}

}

class Neo4jEmbeddedOperationsModifierTest extends OperationsModifierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedOperationsQueryTest extends OperationsQuerierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedProhibitionsModifierTest extends ProhibitionsModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedProhibitionsQuerierTest extends ProhibitionsQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedRoutinesModifierTest extends RoutinesModifierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}

class Neo4jEmbeddedRoutinesQuerierTest extends RoutinesQuerierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return init();
	}

	@AfterAll
	static void teardown() throws IOException {
		File file = new File("/tmp/test");
		FileUtils.deleteDirectory(file);
	}
}
