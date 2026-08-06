package gov.nist.ngac.pm.core.common.tx;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Runs a task on a transaction.
 */
public class TxRunner {

    /**
     * Begins a transaction on the given transactor, runs the given runner, and commits if it succeeds or
     * rolls back if it throws.
     *
     * @param <T> the transactor type
     * @param <V> the runner's result type
     * @param transactor the transactional resource to begin/commit/rollback
     * @param runner the task to run within the transaction
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
     * A functional interface representing the actual execution of a task.
     *
     * @param <T> the result type
     */
    @FunctionalInterface
    public interface Runner<T> {

        /**
         * Runs the task.
         *
         * @return the result
         * @throws PMException if the task fails
         */
        T run() throws PMException;
    }

}
