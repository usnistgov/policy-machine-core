package gov.nist.csd.pm.core.impl.neo4j.embedded.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jEmbeddedPolicyStore;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.core.pap.id.RandomIdGenerator;
import gov.nist.csd.pm.core.pap.modification.GraphModifier;
import gov.nist.csd.pm.core.pap.modification.ObligationsModifier;
import gov.nist.csd.pm.core.pap.modification.OperationsModifier;
import gov.nist.csd.pm.core.pap.modification.PolicyModifier;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModifier;
import gov.nist.csd.pm.core.pap.modification.RoutinesModifier;
import gov.nist.csd.pm.core.pap.query.AccessQuerier;
import gov.nist.csd.pm.core.pap.query.GraphQuerier;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerier;
import gov.nist.csd.pm.core.pap.query.OperationsQuerier;
import gov.nist.csd.pm.core.pap.query.PolicyQuerier;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuerier;
import gov.nist.csd.pm.core.pap.query.RoutinesQuerier;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

public class Neo4jEmbeddedPAP extends PAP {

	public Neo4jEmbeddedPAP(Neo4jEmbeddedPolicyStore store) throws PMException {
		super(initDefault(store));
	}

	public Neo4jEmbeddedPAP(PolicyStore policyStore,
							PolicyModifier modifier,
							PolicyQuerier querier,
							PrivilegeChecker privilegeChecker) throws PMException {
		super(policyStore, modifier, querier, privilegeChecker);
	}

	public Neo4jEmbeddedPAP(PolicyQuerier querier, PolicyModifier modifier, PolicyStore policyStore) throws
                                                                                                     PMException {
		super(querier, modifier, policyStore);
	}

	public Neo4jEmbeddedPAP(PAP pap) throws PMException {
		super(pap);
	}

	private static Neo4jEmbeddedPAP initDefault(Neo4jEmbeddedPolicyStore memoryPolicyStore) throws PMException {
		return initNeo4MemoryPAP(memoryPolicyStore);
	}

	private static Neo4jEmbeddedPAP initNeo4MemoryPAP(Neo4jEmbeddedPolicyStore memoryPolicyStore) throws PMException {
		PolicyModifier policyModifier = new PolicyModifier(
			new GraphModifier(memoryPolicyStore, new RandomIdGenerator()),
			new ProhibitionsModifier(memoryPolicyStore),
			new ObligationsModifier(memoryPolicyStore),
			new OperationsModifier(memoryPolicyStore),
			new RoutinesModifier(memoryPolicyStore)
		);

		PolicyQuerier policyQuerier = new PolicyQuerier(
			new GraphQuerier(memoryPolicyStore),
			new ProhibitionsQuerier(memoryPolicyStore),
			new ObligationsQuerier(memoryPolicyStore),
			new OperationsQuerier(memoryPolicyStore),
			new RoutinesQuerier(memoryPolicyStore),
			new AccessQuerier(memoryPolicyStore)
		);

		PrivilegeChecker privilegeChecker = new PrivilegeChecker(policyQuerier.access());

		return new Neo4jEmbeddedPAP(memoryPolicyStore, policyModifier, policyQuerier, privilegeChecker);
	}
}
