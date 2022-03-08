package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.common.PolicyStore;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.MemTx;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.common.tx.Tx;
import gov.nist.csd.pm.common.tx.TxRunner;

public class MemPAP implements PolicyStore {

    private final GraphAdmin        graphAdmin;
    private final ProhibitionsAdmin prohibitionsAdmin;
    private final ObligationsAdmin  obligationsAdmin;

    public MemPAP(PolicyStore pip) throws PMException {
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
    public void runTx(TxRunner txRunner) throws PMException {
        Tx tx = new MemTx(graphAdmin, prohibitionsAdmin, obligationsAdmin);
        tx.runTx(txRunner);
    }
}
