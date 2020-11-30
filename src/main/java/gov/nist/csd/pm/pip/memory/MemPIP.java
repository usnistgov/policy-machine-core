package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.Features;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.tx.Tx;
import gov.nist.csd.pm.pip.tx.TxRunner;
import gov.nist.csd.pm.pip.memory.tx.MemTx;

public class MemPIP implements Features {

    private final Graph graph;
    private final Prohibitions prohibitions;
    private final Obligations obligations;

    public MemPIP(Graph graph, Prohibitions prohibitions, Obligations obligations) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Graph getGraph() {
        return graph;
    }

    public Prohibitions getProhibitions() {
        return prohibitions;
    }

    public Obligations getObligations() {
        return obligations;
    }

    @Override
    public synchronized void runTx(TxRunner txRunner) throws PMException {
        Tx tx = new MemTx(graph, prohibitions, obligations);
        tx.runTx(txRunner);
    }
}
