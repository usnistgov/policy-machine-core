package gov.nist.csd.pm.pip;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.tx.TxRunner;

public interface Features {

    Graph getGraph();

    Prohibitions getProhibitions();

    Obligations getObligations();

    void runTx(TxRunner txRunner) throws PMException;

}
