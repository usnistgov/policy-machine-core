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

package gov.nist.ngac.pm.core.pap.modification;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;

abstract class Modifier implements Transactional {

    protected PolicyStore policyStore;

    public Modifier(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    public PolicyStore getPolicyStore() {
        return policyStore;
    }

    public void setPolicyStore(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    /**
     * Runs the given task in a transaction, committing on success or rolling back on failure.
     *
     * @return the task's result
     * @throws PMException if the task fails, after the transaction has been rolled back
     */
    protected <T> T runTx(Runner<T> txRunner) throws PMException {
        try {
            beginTx();
            T result = txRunner.run();
            commit();
            return result;
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    /**
     * Runs the given task in a transaction, committing on success or rolling back on failure.
     *
     * @throws PMException if the task fails, after the transaction has been rolled back
     */
    protected void runTx(VoidRunner txRunner) throws PMException {
        try {
            beginTx();
            txRunner.run();
            commit();
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    @Override
    public final void beginTx() throws PMException {
        policyStore.beginTx();
    }

    @Override
    public final void commit() throws PMException {
        policyStore.commit();
    }

    @Override
    public final void rollback() throws PMException {
        policyStore.rollback();
    }

    /**
     * A task run inside a transaction that produces a result.
     */
    public interface Runner<T> {
        T run() throws PMException;
    }

    /**
     * A task run inside a transaction.
     */
    public interface VoidRunner {
        void run() throws PMException;
    }
}
