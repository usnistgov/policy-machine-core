package gov.nist.csd.pm.pap.memory;

public record MemoryTx(boolean active, int counter, TxPolicyStore policyStore) {
}
