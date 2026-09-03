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

package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.List;

/**
 * In memory implementation of the {@link PolicyStore} interface with transaction support. Transactions modify the
 * current state while tracking events in case of rollback.
 */
public class MemoryPolicyStore implements PolicyStore {

    private final TxCmdTracker txCmdTracker;
    private final MemoryTx tx;
    private final MemoryPolicy policy;

    private final MemoryGraphStore graph;
    private final MemoryProhibitionsStore prohibitions;
    private final MemoryObligationsStore obligations;
    private final MemoryOperationsStore operations;

    public MemoryPolicyStore() {
        this.policy = new MemoryPolicy();
        this.tx = new MemoryTx();
        this.txCmdTracker = new TxCmdTracker();

        this.graph = new MemoryGraphStore(policy, tx, txCmdTracker);
        this.prohibitions = new MemoryProhibitionsStore(policy, tx, txCmdTracker);
        this.obligations = new MemoryObligationsStore(policy, tx, txCmdTracker);
        this.operations = new MemoryOperationsStore(policy, tx, txCmdTracker);
    }

    @Override
    public MemoryGraphStore graph() {
        return graph;
    }

    @Override
    public MemoryProhibitionsStore prohibitions() {
        return prohibitions;
    }

    @Override
    public MemoryObligationsStore obligations() {
        return obligations;
    }

    @Override
    public MemoryOperationsStore operations() {
        return operations;
    }

    @Override
    public void reset() {
        policy.reset();
    }

    @Override
    public void beginTx() throws PMException {
        tx.beginTx();
    }

    @Override
    public void commit() {
        tx.commit();

        if (tx.getCounter() == 0) {
            txCmdTracker.clearOps();
        }
    }

    @Override
    public void rollback() throws PMException {
        tx.rollback();

        List<TxCmd> events = txCmdTracker.getOperations();
        for (TxCmd txCmd : events) {
            try {
                txCmd.rollback(this);
            } catch (PMException e) {
                throw new PMException("error during tx rollback", e);
            }
        }

        txCmdTracker.clearOps();
    }
}
