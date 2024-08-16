package gov.nist.csd.pm.pap.tx;

import gov.nist.csd.pm.pap.exception.PMException;

public class TxRunner {

    public static <T extends Transactional> void runTx(T t, Runner<T> runner) throws PMException {
        try {
            t.beginTx();
            runner.run();
            t.commit();
        } catch (PMException e) {
            t.rollback();
            throw e;
        }
    }

    @FunctionalInterface
    public interface Runner<T> {
        void run() throws PMException;
    }

}
