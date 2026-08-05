package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Reverses a single tracked mutation against the in-memory policy store.
 */
public interface TxRollbackSupport {
    void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException;
}
