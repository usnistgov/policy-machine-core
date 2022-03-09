package gov.nist.csd.pm.policy.tx;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.review.PolicyReview;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

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
