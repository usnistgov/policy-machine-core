/**
 * An in-memory, non-durable {@link gov.nist.csd.pm.core.pap.store.PolicyStore} implementation
 * ({@link gov.nist.csd.pm.core.impl.memory.pap.store.MemoryPolicyStore}) and its {@link
 * gov.nist.csd.pm.core.pap.PAP} ({@link gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP}).
 * <p>
 * This backend is intended for testing and single-threaded embedding only. It holds no durable
 * state and is not safe for concurrent transactions from multiple threads — see {@link
 * gov.nist.csd.pm.core.impl.memory.pap.store.MemoryTx} and {@link
 * gov.nist.csd.pm.core.impl.memory.pap.store.ConcurrentTxException}. Production deployments
 * should use a durable backend ({@code impl/neo4j}), a remote PDP over gRPC ({@code impl/grpc}),
 * or a custom {@link gov.nist.csd.pm.core.pap.store.PolicyStore}.
 */
package gov.nist.csd.pm.core.impl.memory.pap;
