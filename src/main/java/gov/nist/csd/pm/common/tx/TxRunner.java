package gov.nist.csd.pm.common.tx;

import gov.nist.csd.pm.common.exception.PMException;

public class TxRunner {

    public static <T extends Transactional, V> V runTx(T transactor, Runner<V> runner) throws PMException {
        try {
            transactor.beginTx();
            V result = runner.run();
            transactor.commit();
            return result;
        } catch (PMException e) {
            transactor.rollback();
            throw e;
        }
    }

    @FunctionalInterface
    public interface Runner<T> {
        T run() throws PMException;
    }

}
