package gov.nist.ngac.pm.core.impl.memory.pap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.ngac.pm.core.pap.operation.NativeOperationRegistry;
import gov.nist.ngac.pm.core.pap.PAP;

/**
 * An implementation of {@link PAP} backed by a {@link MemoryPolicyStore}.
 */
public class MemoryPAP extends PAP {

    public MemoryPAP() throws PMException {
        super(new MemoryPolicyStore());
    }

    public MemoryPAP(MemoryPolicyStore store) throws PMException {
        super(store);
    }

    public MemoryPAP(NativeOperationRegistry nativeOperationRegistry) throws PMException {
        super(new MemoryPolicyStore(), nativeOperationRegistry);
    }

    public MemoryPAP(MemoryPolicyStore store, NativeOperationRegistry nativeOperationRegistry) throws PMException {
        super(store, nativeOperationRegistry);
    }
}
