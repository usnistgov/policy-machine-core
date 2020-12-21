package gov.nist.csd.pm.common;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.common.tx.TxRunner;

public interface FunctionalEntity {

    Graph getGraph() throws PMException;

    Prohibitions getProhibitions() throws PMException;

    Obligations getObligations() throws PMException;

    void runTx(TxRunner txRunner) throws PMException;

}
