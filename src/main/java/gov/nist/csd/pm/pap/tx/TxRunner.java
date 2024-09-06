package gov.nist.csd.pm.pap.tx;

import gov.nist.csd.pm.pap.exception.PMException;

public class TxRunner {

    public static <T extends Transactional> Object runTx(T t, Runner<T> runner) throws PMException {
        try {
            t.beginTx();
            Object result = runner.run();
            t.commit();
            return result;
        } catch (PMException e) {
            t.rollback();
            throw e;
        }
    }

    @FunctionalInterface
    public interface Runner<T> {
        Object run() throws PMException;
    }

}
