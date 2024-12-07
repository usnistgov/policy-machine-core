package gov.nist.csd.pm.impl.neo4j.memory.pap;

import gov.nist.csd.pm.impl.neo4j.memory.pap.query.Neo4jMemoryAccessQuerier;
import gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jMemoryPolicyStore;
import gov.nist.csd.pm.pap.PolicyQuerier;

public class Neo4jMemoryPolicyQuerier extends PolicyQuerier{

	private final Neo4jMemoryAccessQuerier accessQuerier;

	public Neo4jMemoryPolicyQuerier(Neo4jMemoryPolicyStore policyStore) {
		super(policyStore);
		this.accessQuerier = new Neo4jMemoryAccessQuerier(policyStore);
	}

	@Override
	public Neo4jMemoryAccessQuerier access() {
		return accessQuerier;
	}

}
