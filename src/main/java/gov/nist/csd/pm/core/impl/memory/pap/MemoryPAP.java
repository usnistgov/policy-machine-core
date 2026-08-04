package gov.nist.csd.pm.core.impl.memory.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.core.pap.NativeOperationRegistry;
import gov.nist.csd.pm.core.pap.PAP;

/**
 * A {@link PAP} backed by {@link MemoryPolicyStore}, an in-memory, non-durable policy store.
 * <p>
 * This is a convenient way to get started and is well-suited to tests, but it is intended for
 * <b>testing and single-threaded embedding only</b> — it is not a production data store. Policy
 * is not persisted and is lost when the JVM exits, and the store provides no isolation for
 * concurrent access: a transaction started on one thread must be committed or rolled back by
 * that same thread, or a {@link gov.nist.csd.pm.core.impl.memory.pap.store.ConcurrentTxException}
 * is thrown. For production use, prefer the embedded Neo4j backend ({@code impl/neo4j}), the
 * gRPC client to a remote PDP ({@code impl/grpc}), or a custom
 * {@link gov.nist.csd.pm.core.pap.store.PolicyStore} implementation.
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
