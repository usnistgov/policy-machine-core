package gov.nist.csd.pm.core.impl.memory.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.core.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.core.pap.modification.GraphModifier;
import gov.nist.csd.pm.core.pap.modification.ObligationsModifier;
import gov.nist.csd.pm.core.pap.modification.OperationsModifier;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.PolicyModifier;
import gov.nist.csd.pm.core.pap.query.AccessQuerier;
import gov.nist.csd.pm.core.pap.query.GraphQuerier;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerier;
import gov.nist.csd.pm.core.pap.query.OperationsQuerier;
import gov.nist.csd.pm.core.pap.query.PolicyQuerier;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModifier;
import gov.nist.csd.pm.core.pap.modification.RoutinesModifier;
import gov.nist.csd.pm.core.pap.id.RandomIdGenerator;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuerier;
import gov.nist.csd.pm.core.pap.query.RoutinesQuerier;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

public class MemoryPAP extends PAP {

    public MemoryPAP() throws PMException {
        super(initDefault());
    }

    public MemoryPAP(MemoryPolicyStore store) throws PMException {
        super(initDefault(store));
    }

    public MemoryPAP(PolicyStore policyStore,
                     PolicyModifier modifier,
                     PolicyQuerier querier,
                     PrivilegeChecker privilegeChecker) throws PMException {
        super(policyStore, modifier, querier, privilegeChecker);
    }

    public MemoryPAP(PolicyQuerier querier, PolicyModifier modifier, PolicyStore policyStore) throws PMException {
        super(querier, modifier, policyStore);
    }

    public MemoryPAP(PAP pap) throws PMException {
        super(pap);
    }

    private static MemoryPAP initDefault() throws PMException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();

        return initMemoryPAP(memoryPolicyStore);
    }

    private static MemoryPAP initDefault(MemoryPolicyStore memoryPolicyStore) throws PMException {
        return initMemoryPAP(memoryPolicyStore);
    }

    private static MemoryPAP initMemoryPAP(MemoryPolicyStore memoryPolicyStore) throws PMException {
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

        return new MemoryPAP(memoryPolicyStore, policyModifier, policyQuerier, privilegeChecker);
    }
}
