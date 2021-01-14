package gov.nist.csd.pm.pip.memory.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.common.tx.Tx;
import gov.nist.csd.pm.common.tx.TxRunner;

public class MemTx extends Tx {

    private TxGraph txGraph;
    private TxProhibitions txProhibitions;
    private TxObligations txObligations;

    public MemTx(Graph graph, Prohibitions prohibitions, Obligations obligations) {
        super(graph, prohibitions, obligations);
        this.txGraph = new TxGraph(graph);
        this.txProhibitions = new TxProhibitions(prohibitions);
        this.txObligations = new TxObligations(obligations);
    }

    @Override
    public void runTx(TxRunner txRunner) throws PMException {
        try {
            txRunner.run(txGraph, txProhibitions, txObligations);
            commit();
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    public void commit() throws PMException {
        synchronized (graph) {
            synchronized (prohibitions) {
                synchronized (obligations) {
                    // commit the graph
                    txGraph.commit();

                    // commit the prohibitions
                    txProhibitions.commit();

                    // commit the obligations
                    txObligations.commit();
                }
            }
        }
    }

    public void rollback() {
        // rollback graph
        txGraph = new TxGraph(graph);

        // rollback prohibitions
        txProhibitions = new TxProhibitions(prohibitions);

        // rollback obligations
        txObligations = new TxObligations(obligations);
    }
}
