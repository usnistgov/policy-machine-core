package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;

public interface TxRollbackSupport {
    void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException;
}
