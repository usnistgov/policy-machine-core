package gov.nist.csd.pm.core.impl.memory.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.core.pap.PAP;

public class MemoryPAP extends PAP {

    public MemoryPAP() throws PMException {
        super(new MemoryPolicyStore());
    }

    public MemoryPAP(MemoryPolicyStore store) throws PMException {
        super(store);
    }
}
