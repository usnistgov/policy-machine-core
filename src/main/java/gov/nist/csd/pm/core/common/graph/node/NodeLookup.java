package gov.nist.csd.pm.core.common.graph.node;

import gov.nist.csd.pm.core.common.exception.PMException;

@FunctionalInterface
public interface NodeLookup {

    Node getNodeByName(String name) throws PMException;

}