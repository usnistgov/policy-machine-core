package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;

public interface NodeLookup {

    Node getNodeByName(String name) throws PMException;

    Node getNodeById(long id) throws PMException;

}
