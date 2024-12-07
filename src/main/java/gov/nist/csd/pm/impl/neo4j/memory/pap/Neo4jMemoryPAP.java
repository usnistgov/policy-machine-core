package gov.nist.csd.pm.impl.neo4j.memory.pap;

import gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jMemoryPolicyStore;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.PolicyQuery;

public class Neo4jMemoryPAP extends PAP {

	private Neo4jMemoryPolicyStore policyStore;
	private Neo4jMemoryPolicyQuerier policyQuerier;

	public Neo4jMemoryPAP(Neo4jMemoryPolicyStore policyStore) throws PMException {
		super(policyStore);

		this.policyStore = policyStore;
		this.policyQuerier = new Neo4jMemoryPolicyQuerier(policyStore);
	}

	@Override
	public PolicyQuery query() {
		return policyQuerier;
	}

	@Override
	public void beginTx() throws PMException {
		policyStore.beginTx();
	}

	@Override
	public void commit() throws PMException {
		policyStore.commit();
	}

	@Override
	public void rollback() throws PMException {
		policyStore.rollback();
	}
}
