package gov.nist.csd.pm.core.impl.memory.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.id.RandomIdGenerator;
import gov.nist.csd.pm.core.pap.modification.GraphModifier;
import gov.nist.csd.pm.core.pap.modification.ObligationsModifier;
import gov.nist.csd.pm.core.pap.modification.OperationsModifier;
import gov.nist.csd.pm.core.pap.modification.PolicyModifier;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModifier;
import gov.nist.csd.pm.core.pap.operation.PluginRegistry;
import gov.nist.csd.pm.core.pap.operation.PrivilegeChecker;
import gov.nist.csd.pm.core.pap.query.AccessQuerier;
import gov.nist.csd.pm.core.pap.query.GraphQuerier;
import gov.nist.csd.pm.core.pap.query.ObligationsQuerier;
import gov.nist.csd.pm.core.pap.query.OperationsQuerier;
import gov.nist.csd.pm.core.pap.query.PolicyQuerier;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuerier;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

public class MemoryPAP extends PAP {

    public MemoryPAP() throws PMException {
        super(new MemoryPolicyStore());
    }

    public MemoryPAP(MemoryPolicyStore store) throws PMException {
        super(store);
    }
}
