package gov.nist.csd.pm.common.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public abstract class Tx {

    public final Graph graph;
    public final Prohibitions prohibitions;
    public final Obligations obligations;

    public Tx(Graph graph, Prohibitions prohibitions, Obligations obligations) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public abstract void runTx(TxRunner txRunner) throws PMException;

    public abstract void commit() throws PMException;

    public abstract void rollback() throws PMException;

}
