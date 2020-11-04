package gov.nist.csd.pm.pip;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.tx.Tx;
import gov.nist.csd.pm.pip.tx.TxRunner;
import gov.nist.csd.pm.pip.tx.memory.MemTx;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemPIP implements PIP, Tx {

    private MemGraph graph;
    private MemProhibitions prohibitions;
    private MemObligations obligations;
    private ReadWriteLock graphLock;
    private ReadWriteLock prohibitionsLock;
    private ReadWriteLock obligationsLock;

    public MemPIP(MemGraph graph, MemProhibitions prohibitions, MemObligations obligations) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;

        this.graphLock = new ReentrantReadWriteLock();
        this.prohibitionsLock = new ReentrantReadWriteLock();
        this.obligationsLock = new ReentrantReadWriteLock();
    }

    public MemGraph getGraph() {
        return graph;
    }

    public void setGraph(MemGraph graph) {
        this.graph = graph;
    }

    public MemProhibitions getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(MemProhibitions prohibitions) {
        this.prohibitions = prohibitions;
    }

    public MemObligations getObligations() {
        return obligations;
    }

    public void setObligations(MemObligations obligations) {
        this.obligations = obligations;
    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    public void runTx(TxRunner txRunner) {
        MemTx tx = new MemTx(getGraph(), getProhibitions(), getObligations());
        try {
            // run the transaction on the given TxRunner
            tx.runTx(txRunner);

            // commit the transaction
            tx.commit();
        } catch (PMException e) {
            // rollback the tx if exception occurred
            tx.rollback();
        }
    }
}
