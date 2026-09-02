// This file is part of the policy-machine-core-neo4j module, which links against
// org.neo4j:neo4j (GPLv3) and is distributed under GPLv3. See neo4j/LICENSE.
package gov.nist.ngac.pm.core.neo4j.embedded.pap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jEmbeddedPolicyStore;
import gov.nist.ngac.pm.core.pap.operation.JavaOperationRegistry;
import gov.nist.ngac.pm.core.pap.PAP;

/**
 * A {@link PAP} backed by an embedded Neo4j database.
 */
public class Neo4jEmbeddedPAP extends PAP {

	public Neo4jEmbeddedPAP(Neo4jEmbeddedPolicyStore store) throws PMException {
		super(store);
	}

	public Neo4jEmbeddedPAP(Neo4jEmbeddedPolicyStore store, JavaOperationRegistry javaOperationRegistry) throws PMException {
		super(store, javaOperationRegistry);
	}
}
