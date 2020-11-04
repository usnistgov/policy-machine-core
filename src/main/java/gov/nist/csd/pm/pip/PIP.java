package gov.nist.csd.pm.pip;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.tx.TxRunner;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public interface PIP {

    Graph getGraph() throws PMException;

    Prohibitions getProhibitions() throws PMException;

    Obligations getObligations() throws PMException;

}
