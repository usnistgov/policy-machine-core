package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;

public interface TxRollbackSupport {
    void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException;
}
