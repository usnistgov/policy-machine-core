package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface BaseMemoryTx {
    void rollback() throws PMException;
}
