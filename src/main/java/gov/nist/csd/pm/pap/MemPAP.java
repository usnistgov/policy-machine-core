package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.Features;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.MemTx;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import gov.nist.csd.pm.pip.tx.Tx;
import gov.nist.csd.pm.pip.tx.TxRunner;

public class MemPAP implements Features {

    private final GraphAdmin        graphAdmin;
    private final ProhibitionsAdmin prohibitionsAdmin;
    private final ObligationsAdmin  obligationsAdmin;

    public MemPAP(Features pip) throws PMException {
        this.graphAdmin = new GraphAdmin(pip);
        this.prohibitionsAdmin = new ProhibitionsAdmin(pip);
        this.obligationsAdmin = new ObligationsAdmin(pip);
    }

    @Override
    public Graph getGraph() {
        return graphAdmin;
    }

    @Override
    public Prohibitions getProhibitions() {
        return prohibitionsAdmin;
    }

    @Override
    public Obligations getObligations() {
        return obligationsAdmin;
    }

    @Override
    public synchronized void runTx(TxRunner txRunner) throws PMException {
        Tx tx = new MemTx(graphAdmin, prohibitionsAdmin, obligationsAdmin);
        tx.runTx(txRunner);
    }
}
