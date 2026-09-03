/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST.
 *
 * This file is part of the policy-machine-core-neo4j module, which compiles against
 * and embeds org.neo4j:neo4j (GPLv3, Community Edition). As a combined work, this
 * module is distributed under the GNU General Public License v3.0, not the CC BY 4.0
 * license used elsewhere in this repository. See neo4j/LICENSE for the full text.
 */

package gov.nist.ngac.pm.core.neo4j.embedded.pap;

import static gov.nist.ngac.pm.core.neo4j.embedded.pap.Neo4jTestInitializer.init;
import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jEmbeddedPolicyStore;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.PAPTest;
import gov.nist.ngac.pm.core.pap.modification.GraphModifierTest;
import gov.nist.ngac.pm.core.pap.modification.ObligationsModifierTest;
import gov.nist.ngac.pm.core.pap.modification.OperationsModifierTest;
import gov.nist.ngac.pm.core.pap.modification.ProhibitionsModifierTest;
import gov.nist.ngac.pm.core.pap.query.AccessQuerierTest;
import gov.nist.ngac.pm.core.pap.query.GraphQuerierTest;
import gov.nist.ngac.pm.core.pap.query.ObligationsQuerierTest;
import gov.nist.ngac.pm.core.pap.query.OperationsQuerierTest;
import gov.nist.ngac.pm.core.pap.query.ProhibitionsQuerierTest;
import gov.nist.ngac.pm.core.pap.query.RoutinesQuerierTest;
import gov.nist.ngac.pm.core.util.TestIdGenerator;
import java.nio.file.Path;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

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
		return new Neo4jEmbeddedPAP(
			new Neo4jEmbeddedPolicyStore(Neo4jTestInitializer.getTx(tempDir))
		).withIdGenerator(new TestIdGenerator());
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
class Neo4jEmbeddedRoutinesQuerierTest extends RoutinesQuerierTest {
	@TempDir
	private Path tempDir;

	@Override
	public PAP initializePAP() throws PMException {
		return init(tempDir);
	}
}
