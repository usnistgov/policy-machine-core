package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.common.tx.Tx;
import gov.nist.csd.pm.common.tx.TxRunner;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.MemTx;
import gov.nist.csd.pm.pip.memory.tx.MySQLTx;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class MySQLPIP implements FunctionalEntity {

    private final Graph graph;
    private final Prohibitions prohibitions;
    private final Obligations obligations;

    public MySQLPIP(Graph graph, Prohibitions prohibitions, Obligations obligations) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    @Override
    public Graph getGraph() throws PMException {
        return graph;
    }

    @Override
    public Prohibitions getProhibitions() throws PMException {
        return prohibitions;
    }

    @Override
    public Obligations getObligations() throws PMException {
        return obligations;
    }

    @Override
    public void runTx(TxRunner txRunner) throws PMException {
        Tx tx = new MySQLTx(graph, prohibitions, obligations);
        tx.runTx(txRunner);
    }
}
