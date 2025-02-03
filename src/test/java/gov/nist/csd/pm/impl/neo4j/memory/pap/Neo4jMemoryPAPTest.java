package gov.nist.csd.pm.impl.neo4j.memory.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jMemoryPolicyStore;
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
			Neo4jMemoryPolicyStore.createIndexes(graphDb);
		}

		try(Transaction tx = graphDb.beginTx()) {
			tx.execute("match (n) detach delete n");
			tx.commit();
		}
		
		return graphDb;
	}
}

public class Neo4jMemoryPAPTest extends PAPTest {
	
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryAccessQuerierTest extends AccessQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryGraphModifierTest extends GraphModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryGraphQuerierTest extends GraphQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryObligationsModifierTest extends ObligationsModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryObligationsQuerierTest extends ObligationsQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}

}

class MemoryOperationsModifierTest extends OperationsModifierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryOperationsQueryTest extends OperationsQuerierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryProhibitionsModifierTest extends ProhibitionsModifierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryProhibitionsQuerierTest extends ProhibitionsQuerierTest {

	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryRoutinesModifierTest extends RoutinesModifierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}

class MemoryRoutinesQuerierTest extends RoutinesQuerierTest {
	@Override
	public PAP initializePAP() throws PMException {
		return new Neo4jMemoryPAP(new Neo4jMemoryPolicyStore(TestTx.getTx()))
				.withIdGenerator(new TestIdGenerator());
	}
}
