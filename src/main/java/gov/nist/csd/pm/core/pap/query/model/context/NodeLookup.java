package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;

@FunctionalInterface
public interface NodeLookup {

    Node getNodeByName(String name) throws PMException;

}
