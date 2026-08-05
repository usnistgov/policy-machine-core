package gov.nist.csd.pm.core.impl.memory.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.core.pap.NativeOperationRegistry;
import gov.nist.csd.pm.core.pap.PAP;

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
