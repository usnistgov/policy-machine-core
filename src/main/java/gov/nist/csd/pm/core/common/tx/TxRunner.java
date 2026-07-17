package gov.nist.csd.pm.core.common.tx;

import gov.nist.csd.pm.core.common.exception.PMException;

public class TxRunner {

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

    @FunctionalInterface
    public interface Runner<T> {
        T run() throws PMException;
    }

}
