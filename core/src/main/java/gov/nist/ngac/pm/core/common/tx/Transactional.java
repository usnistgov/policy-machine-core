package gov.nist.ngac.pm.core.common.tx;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Something that supports transactional begin/commit/rollback, e.g. a store backed by a database
 * connection.
 */
public interface Transactional {

    /**
     * Begins a new transaction.
     *
     * @throws PMException if a transaction is already in progress or the underlying resource fails to begin one
     */
    void beginTx() throws PMException;

    /**
     * Commits the current transaction.
     *
     * @throws PMException if there is no transaction in progress or the commit fails
     */
    void commit() throws PMException;

    /**
     * Rolls back the current transaction.
     *
     * @throws PMException if there is no transaction in progress or the rollback fails
     */
    void rollback() throws PMException;

}
