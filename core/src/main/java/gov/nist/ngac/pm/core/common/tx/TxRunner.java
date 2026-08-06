package gov.nist.ngac.pm.core.common.tx;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Runs a unit of work within a {@link Transactional}'s transaction, committing on success and rolling back
 * on failure.
 */
public class TxRunner {

    /**
     * Begins a transaction on the given transactor, runs the given runner, and commits if it succeeds or
     * rolls back if it throws.
     *
     * @param <T> the transactor type
     * @param <V> the runner's result type
     * @param transactor the transactional resource to begin/commit/rollback
     * @param runner the unit of work to run within the transaction
     * @return the runner's result
     * @throws PMException if the runner throws, or if begin/commit/rollback fails
     */
    public static <T extends Transactional, V> V runTx(T transactor, Runner<V> runner) throws PMException {
        try {
            transactor.beginTx();
            V result = runner.run();
            transactor.commit();
            return result;
        } catch (Exception e) {
            transactor.rollback();
            if (e instanceof PMException pmException) {
                throw pmException;
            }
            throw new PMException(e);
        }
    }

    /**
     * A unit of work to run within a transaction managed by {@link TxRunner#runTx}.
     *
     * @param <T> the result type
     */
    @FunctionalInterface
    public interface Runner<T> {

        /**
         * Runs the unit of work.
         *
         * @return the result
         * @throws PMException if the work fails
         */
        T run() throws PMException;
    }

}
