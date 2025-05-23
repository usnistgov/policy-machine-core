package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;

public interface TxRollbackSupport {
    void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException;
}
