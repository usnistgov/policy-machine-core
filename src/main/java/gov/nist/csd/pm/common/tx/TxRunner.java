package gov.nist.csd.pm.common.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public interface TxRunner {
    void run(Graph graph, Prohibitions prohibitions, Obligations obligations) throws PMException;
}
