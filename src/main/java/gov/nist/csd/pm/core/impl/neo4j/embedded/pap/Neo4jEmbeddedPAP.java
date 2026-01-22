package gov.nist.csd.pm.core.impl.neo4j.embedded.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jEmbeddedPolicyStore;
import gov.nist.csd.pm.core.pap.PAP;

public class Neo4jEmbeddedPAP extends PAP {

	public Neo4jEmbeddedPAP(Neo4jEmbeddedPolicyStore store) throws PMException {
		super(store);
	}
}
