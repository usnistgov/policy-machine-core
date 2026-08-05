package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;

public interface NodeLookup {

    Node getNodeByName(String name) throws PMException;

    Node getNodeById(long id) throws PMException;

}
