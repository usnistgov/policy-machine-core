package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.common.tx.Tx;
import gov.nist.csd.pm.common.tx.TxRunner;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.MySQLTx;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class MySQLPAP implements FunctionalEntity {

    private final GraphAdmin        graphAdmin;
    private final ProhibitionsAdmin prohibitionsAdmin;
    private final ObligationsAdmin  obligationsAdmin;

    public MySQLPAP(FunctionalEntity pip) throws PMException {
        this.graphAdmin = new GraphAdmin(pip);
        this.prohibitionsAdmin = new ProhibitionsAdmin(pip);
        this.obligationsAdmin = new ObligationsAdmin(pip);
    }

    @Override
    public Graph getGraph() throws PMException {
        return graphAdmin;
    }

    @Override
    public Prohibitions getProhibitions() throws PMException {
        return prohibitionsAdmin;
    }

    @Override
    public Obligations getObligations() throws PMException {
        return obligationsAdmin;
    }

    @Override
    public void runTx(TxRunner txRunner) throws PMException {
        Tx tx = new MySQLTx(graphAdmin, prohibitionsAdmin, obligationsAdmin);
        tx.runTx(txRunner);
    }
}
