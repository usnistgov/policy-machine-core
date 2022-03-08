package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.common.PolicyStore;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.common.tx.Tx;
import gov.nist.csd.pm.common.tx.TxRunner;
import gov.nist.csd.pm.pip.memory.tx.MemTx;

public class MemoryPolicyStore implements PolicyStore {

    private final Graph graph;
    private final Prohibitions prohibitions;
    private final Obligations obligations;

    public MemoryPolicyStore() {
        this.graph = new MemGraph();
        this.prohibitions = new MemProhibitions();
        this.obligations = new MemObligations();
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public Prohibitions getProhibitions() {
        return prohibitions;
    }

    @Override
    public Obligations getObligations() {
        return obligations;
    }

    @Override
    public void runTx(TxRunner txRunner) throws PMException {
        Tx tx = new MemTx(graph, prohibitions, obligations);
        tx.runTx(txRunner);
    }
}
