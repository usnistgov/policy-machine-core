/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.JavaOperationRegistry;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.store.GraphStore;
import gov.nist.ngac.pm.core.pap.store.ObligationsStore;
import gov.nist.ngac.pm.core.pap.store.OperationsStore;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import gov.nist.ngac.pm.core.pap.store.ProhibitionsStore;
import org.junit.jupiter.api.Test;

/**
 * {@link OperationsQuerier#getOperation(String)} must resolve a protected built-in entirely from the
 * {@link JavaOperationRegistry}, per design doc §6 -- "a protected built-in resolves without any store
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
        JavaOperationRegistry registry = new JavaOperationRegistry();
        OperationsQuerier querier = new OperationsQuerier(throwingStore(), registry);

        Operation<?> resolved = querier.getOperation("assign");

        assertEquals(registry.get("assign"), resolved);
    }
}
