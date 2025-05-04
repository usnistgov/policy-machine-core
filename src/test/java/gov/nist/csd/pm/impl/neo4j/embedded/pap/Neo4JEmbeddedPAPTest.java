package gov.nist.csd.pm.impl.neo4j.embedded.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.neo4j.embedded.pap.store.Neo4jEmbeddedPolicyStore;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTest;
import gov.nist.csd.pm.pap.modification.*;
import gov.nist.csd.pm.pap.query.*;
import gov.nist.csd.pm.util.TestIdGenerator;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.io.File;

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
}

public class Neo4JEmbeddedPAPTest extends PAPTest {
	
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedAccessQuerierTest extends AccessQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedGraphModifierTest extends GraphModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedGraphQuerierTest extends GraphQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedObligationsModifierTest extends ObligationsModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedObligationsQuerierTest extends ObligationsQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}

}

class Neo4jEmbeddedOperationsModifierTest extends OperationsModifierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedOperationsQueryTest extends OperationsQuerierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedProhibitionsModifierTest extends ProhibitionsModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedProhibitionsQuerierTest extends ProhibitionsQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedRoutinesModifierTest extends RoutinesModifierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class Neo4jEmbeddedRoutinesQuerierTest extends RoutinesQuerierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jEmbeddedPAP(new Neo4jEmbeddedPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}
