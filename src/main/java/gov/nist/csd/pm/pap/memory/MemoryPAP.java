package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;

public class MemoryPAP extends PAP {
    public MemoryPAP() throws PMException {
        super(new MemoryConnection(new MemoryPolicyStore()));
    }
}
