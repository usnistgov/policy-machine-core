package gov.nist.csd.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.NativeOperationRegistry;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import gov.nist.csd.pm.core.pap.store.ObligationsStore;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import gov.nist.csd.pm.core.pap.store.ProhibitionsStore;
import org.junit.jupiter.api.Test;

/**
 * {@link OperationsQuerier#getOperation(String)} must resolve a protected built-in entirely from the
 * {@link NativeOperationRegistry}, per design doc §6 -- "a protected built-in resolves without any store
 * round-trip". Wires a {@link PolicyStore} whose every method throws to prove this, since a real backend
 * (Memory or Neo4j) can't itself distinguish "resolved without touching the store" from "resolved after an
 * incidental store call that happened not to matter".
 */
class OperationsQuerierUnitTest {

    private static PolicyStore throwingStore() {
        return new PolicyStore() {
            @Override
            public GraphStore graph() {
                throw new AssertionError("getOperation for a protected built-in must not touch the store");
            }

            @Override
            public ProhibitionsStore prohibitions() {
                throw new AssertionError("getOperation for a protected built-in must not touch the store");
            }

            @Override
            public ObligationsStore obligations() {
                throw new AssertionError("getOperation for a protected built-in must not touch the store");
            }

            @Override
            public OperationsStore operations() {
                throw new AssertionError("getOperation for a protected built-in must not touch the store");
            }

            @Override
            public void reset() {
                throw new AssertionError("getOperation for a protected built-in must not touch the store");
            }

            @Override
            public void beginTx() {
            }

            @Override
            public void commit() {
            }

            @Override
            public void rollback() {
            }
        };
    }

    @Test
    void testProtectedBuiltinResolvesWithoutStoreRoundTrip() throws PMException {
        NativeOperationRegistry registry = new NativeOperationRegistry();
        OperationsQuerier querier = new OperationsQuerier(throwingStore(), registry);

        Operation<?> resolved = querier.getOperation("assign");

        assertEquals(registry.get("assign"), resolved);
    }
}
